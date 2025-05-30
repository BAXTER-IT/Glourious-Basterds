package com.eurhufapp.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.eurhufapp.exception.ApiException;
import com.eurhufapp.model.ExchangeRate;

import reactor.core.publisher.Mono;

/**
 * Service for fetching EURHUF exchange rate data from Exchange Rate API.
 */
@Service
public class ExchangeRateApiService {
    
    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateApiService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private final WebClient webClient;
    
    @Value("${ecb.api.base}")
    private String baseCurrency;
    
    @Value("${ecb.api.symbols}")
    private String symbols;
    
    /**
     * Constructs a new ExchangeRateApiService with the specified API URL.
     * 
     * @param apiUrl The API URL
     */
    public ExchangeRateApiService(@Value("${ecb.api.url}") String apiUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .build();
    }
    
    /**
     * Fetches the current EURHUF exchange rate.
     * 
     * @return The current exchange rate
     * @throws ApiException if there is an error fetching the data
     */
    public ExchangeRate getCurrentRate() {
        logger.debug("Fetching current exchange rate for {}{}", baseCurrency, symbols);
        
        try {
            String uri = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("base", baseCurrency)
                            .queryParam("symbols", symbols)
                            .build())
                    .toString();
            
            logger.debug("Making request to: {}", uri);
            
            ExchangeRateResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("base", baseCurrency)
                            .queryParam("symbols", symbols)
                            .build())
                    .retrieve()
                    .bodyToMono(ExchangeRateResponse.class)
                    .doOnNext(resp -> logger.debug("Received response: {}", resp))
                    .block();
            
            if (response == null) {
                logger.error("Received null response from API");
                throw new ApiException("Failed to fetch exchange rate data from API: null response");
            }
            
            if (response.getRates() == null) {
                logger.error("Response rates is null: {}", response);
                throw new ApiException("Failed to fetch exchange rate data from API: null rates");
            }
            
            logger.debug("Response: base={}, date={}, rates={}", response.getBase(), response.getDate(), response.getRates());
            
            Double rate = response.getRates().get(symbols);
            if (rate == null) {
                logger.error("Rate not found for symbol {}: {}", symbols, response.getRates());
                throw new ApiException("Exchange rate data not found for " + symbols);
            }
            
            return ExchangeRate.builder()
                    .timestamp(LocalDateTime.now())
                    .rate(rate)
                    .build();
        } catch (Exception e) {
            logger.error("Error fetching current exchange rate", e);
            throw new ApiException("Error fetching current exchange rate: " + e.getMessage(), e);
        }
    }
    
    /**
     * Fetches historical EURHUF exchange rate data.
     * 
     * @param from The start date
     * @param to The end date
     * @param interval The interval (e.g., "1d", "1w", "1m")
     * @return A list of historical exchange rates
     * @throws ApiException if there is an error fetching the data
     */
    public List<ExchangeRate> getHistoricalData(LocalDateTime from, LocalDateTime to, String interval) {
        logger.debug("Fetching historical data from {} to {} with interval {}", from, to, interval);
        
        List<ExchangeRate> rates = new ArrayList<>();
        LocalDateTime current = from;
        
        // Determine the time step based on the interval
        ChronoUnit unit;
        int step;
        
        switch (interval) {
            case "1h":
                unit = ChronoUnit.HOURS;
                step = 1;
                break;
            case "1d":
                unit = ChronoUnit.DAYS;
                step = 1;
                break;
            case "1w":
                unit = ChronoUnit.WEEKS;
                step = 1;
                break;
            case "1m":
                unit = ChronoUnit.MONTHS;
                step = 1;
                break;
            default:
                unit = ChronoUnit.DAYS;
                step = 1;
        }
        
        logger.debug("Using time step: {} {}", step, unit);
        
        while (!current.isAfter(to)) {
            try {
                String date = current.format(DATE_FORMATTER);
                logger.debug("Fetching data for date: {}", date);
                
                String uri = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/" + date)
                                .queryParam("base", baseCurrency)
                                .queryParam("symbols", symbols)
                                .build())
                        .toString();
                
                logger.debug("Making request to: {}", uri);
                
                // Frankfurter API format: /YYYY-MM-DD?from=EUR&to=HUF
                ExchangeRateResponse response = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/" + date)
                                .queryParam("from", baseCurrency)
                                .queryParam("to", symbols)
                                .build())
                        .retrieve()
                        .bodyToMono(ExchangeRateResponse.class)
                        .doOnNext(resp -> logger.debug("Received response for {}: {}", date, resp))
                        .block();
                
                if (response == null) {
                    logger.warn("Received null response for date {}", date);
                } else if (response.getRates() == null) {
                    logger.warn("Response rates is null for date {}: {}", date, response);
                } else {
                    Double rate = response.getRates().get(symbols);
                    if (rate != null) {
                        logger.debug("Got rate for {}: {}", date, rate);
                        rates.add(ExchangeRate.builder()
                                .timestamp(current)
                                .rate(rate)
                                .build());
                    } else {
                        logger.warn("Rate not found for symbol {} on date {}: {}", symbols, date, response.getRates());
                    }
                }
                
                // Move to the next time step
                current = current.plus(step, unit);
            } catch (Exception e) {
                logger.error("Error fetching historical data for date {}", current, e);
                // Continue with the next date
                current = current.plus(step, unit);
            }
        }
        
        logger.debug("Fetched {} historical data points", rates.size());
        return rates;
    }
    
    /**
     * Response class for the Frankfurter API.
     * Example response:
     * {
     *   "amount": 1.0,
     *   "base": "EUR",
     *   "date": "2023-05-30",
     *   "rates": {
     *     "HUF": 385.42
     *   }
     * }
     */
    private static class ExchangeRateResponse {
        private double amount;
        private String base;
        private String date;
        private Map<String, Double> rates;
        
        // We consider the response successful if it has rates
        public boolean isSuccess() {
            return rates != null && !rates.isEmpty();
        }
        
        public double getAmount() {
            return amount;
        }
        
        public void setAmount(double amount) {
            this.amount = amount;
        }
        
        public String getBase() {
            return base;
        }
        
        public void setBase(String base) {
            this.base = base;
        }
        
        public String getDate() {
            return date;
        }
        
        public void setDate(String date) {
            this.date = date;
        }
        
        public Map<String, Double> getRates() {
            return rates;
        }
        
        public void setRates(Map<String, Double> rates) {
            this.rates = rates;
        }
        
        @Override
        public String toString() {
            return "ExchangeRateResponse{" +
                    "amount=" + amount +
                    ", base='" + base + '\'' +
                    ", date='" + date + '\'' +
                    ", rates=" + rates +
                    '}';
        }
    }
}