package com.eurhufapp.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eurhufapp.model.ExchangeRate;
import com.eurhufapp.model.TimeSeriesData;
import com.eurhufapp.service.ExchangeRateService;

/**
 * REST controller for exchange rate data.
 * Provides endpoints for fetching current and historical exchange rates.
 */
@RestController
@RequestMapping("/eurhuf")
public class ExchangeRateController {
    
    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateController.class);
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    
    private final ExchangeRateService exchangeRateService;
    
    /**
     * Constructs a new ExchangeRateController with the specified service.
     * 
     * @param exchangeRateService The exchange rate service
     */
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }
    
    /**
     * Gets the current EURHUF exchange rate.
     * 
     * @return The current exchange rate
     */
    @GetMapping("/current")
    public ResponseEntity<ExchangeRate> getCurrentRate() {
        logger.info("GET /eurhuf/current");
        ExchangeRate rate = exchangeRateService.getCurrentRate();
        return ResponseEntity.ok(rate);
    }
    
    /**
     * Gets historical EURHUF exchange rate data.
     * 
     * @param fromStr The start date (ISO format)
     * @param toStr The end date (ISO format)
     * @param interval The interval (e.g., "1h", "1d", "1w")
     * @return A TimeSeriesData object containing the historical data
     */
    @GetMapping("/historical")
    public ResponseEntity<TimeSeriesData> getHistoricalData(
            @RequestParam("from") String fromStr,
            @RequestParam("to") String toStr,
            @RequestParam(value = "interval", defaultValue = "1d") String interval) {
        
        logger.info("GET /eurhuf/historical?from={}&to={}&interval={}", fromStr, toStr, interval);
        
        try {
            LocalDateTime from = LocalDateTime.parse(fromStr, ISO_FORMATTER);
            LocalDateTime to = LocalDateTime.parse(toStr, ISO_FORMATTER);
            
            TimeSeriesData data = exchangeRateService.getHistoricalData(from, to, interval);
            return ResponseEntity.ok(data);
        } catch (DateTimeParseException e) {
            logger.error("Invalid date format", e);
            throw new IllegalArgumentException("Invalid date format. Use ISO format (e.g., 2025-05-30T09:00:00)");
        }
    }
    
    /**
     * Gets EURHUF exchange rate data for a specific time range.
     * 
     * @param range The time range (e.g., "1d", "1w", "1m", "3m")
     * @return A TimeSeriesData object containing the data for the specified range
     */
    @GetMapping("/range/{range}")
    public ResponseEntity<TimeSeriesData> getRangeData(@PathVariable("range") String range) {
        logger.info("GET /eurhuf/range/{}", range);
        
        TimeSeriesData data = exchangeRateService.getRangeData(range);
        return ResponseEntity.ok(data);
    }
}