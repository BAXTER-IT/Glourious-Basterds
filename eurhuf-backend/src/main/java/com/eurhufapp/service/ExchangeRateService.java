package com.eurhufapp.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.eurhufapp.config.CacheConfig;
import com.eurhufapp.model.ExchangeRate;
import com.eurhufapp.model.TimeSeriesData;
import com.eurhufapp.repository.ExchangeRateRepository;

/**
 * Service for handling exchange rate data.
 * Provides methods for fetching current and historical exchange rates.
 */
@Service
public class ExchangeRateService {
    
    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);
    
    private final ExchangeRateApiService exchangeRateApiService;
    private final ExchangeRateRepository exchangeRateRepository;
    private final CacheService cacheService;
    
    @Value("${ecb.api.update.interval:60000}")
    private long updateInterval;
    
    /**
     * Constructs a new ExchangeRateService with the specified dependencies.
     *
     * @param exchangeRateApiService The Exchange Rate API service
     * @param exchangeRateRepository The exchange rate repository
     * @param cacheService The cache service
     */
    public ExchangeRateService(
            ExchangeRateApiService exchangeRateApiService,
            ExchangeRateRepository exchangeRateRepository,
            CacheService cacheService) {
        this.exchangeRateApiService = exchangeRateApiService;
        this.exchangeRateRepository = exchangeRateRepository;
        this.cacheService = cacheService;
    }
    
    /**
     * Fetches the current EURHUF exchange rate.
     * This method is cached to minimize external API calls.
     *
     * @return The current exchange rate
     */
    @Cacheable(CacheConfig.CURRENT_RATE_CACHE)
    public ExchangeRate getCurrentRate() {
        logger.debug("Fetching current exchange rate");
        ExchangeRate rate = exchangeRateApiService.getCurrentRate();
        exchangeRateRepository.save(rate);
        return rate;
    }
    
    /**
     * Fetches historical EURHUF exchange rate data.
     * This method is cached to minimize external API calls.
     *
     * @param from The start date
     * @param to The end date
     * @param interval The interval (e.g., "1h", "1d", "1w")
     * @return A TimeSeriesData object containing the historical data
     */
    @Cacheable(value = CacheConfig.HISTORICAL_DATA_CACHE, key = "#from.toString() + #to.toString() + #interval")
    public TimeSeriesData getHistoricalData(LocalDateTime from, LocalDateTime to, String interval) {
        logger.debug("Fetching historical data from {} to {} with interval {}", from, to, interval);
        
        List<ExchangeRate> rates = exchangeRateApiService.getHistoricalData(from, to, interval);
        exchangeRateRepository.saveAll(rates);
        
        return TimeSeriesData.builder()
                .data(rates)
                .meta(TimeSeriesData.TimeSeriesMeta.builder()
                        .interval(interval)
                        .count(rates.size())
                        .build())
                .build();
    }
    
    /**
     * Fetches EURHUF exchange rate data for a specific time range.
     * This method is cached to minimize external API calls.
     *
     * @param range The time range (e.g., "1d", "1w", "1m", "3m")
     * @return A TimeSeriesData object containing the data for the specified range
     */
    @Cacheable(value = CacheConfig.RANGE_DATA_CACHE, key = "#range")
    public TimeSeriesData getRangeData(String range) {
        logger.debug("Fetching data for range {}", range);
        
        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from;
        String interval;
        
        switch (range) {
            case "1d":
                from = to.minus(1, ChronoUnit.DAYS);
                interval = "1h";
                break;
            case "1w":
                from = to.minus(7, ChronoUnit.DAYS);
                interval = "1d";
                break;
            case "1m":
                from = to.minus(1, ChronoUnit.MONTHS);
                interval = "1d";
                break;
            case "3m":
                from = to.minus(3, ChronoUnit.MONTHS);
                interval = "1d";
                break;
            default:
                throw new IllegalArgumentException("Invalid range: " + range);
        }
        
        return getHistoricalData(from, to, interval);
    }
    
    /**
     * Scheduled task to update the current exchange rate at regular intervals.
     * The interval is configured in application.properties.
     */
    @Scheduled(fixedRateString = "${ecb.api.update.interval:60000}")
    public void updateCurrentRate() {
        logger.debug("Scheduled update of current exchange rate");
        if (cacheService.needsUpdate(CacheConfig.CURRENT_RATE_CACHE)) {
            cacheService.clearCache(CacheConfig.CURRENT_RATE_CACHE);
            getCurrentRate();
        }
    }
}