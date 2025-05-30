package com.eurhufapp.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.eurhufapp.model.ExchangeRate;

/**
 * Repository interface for accessing exchange rate data.
 * This is implemented with an in-memory storage solution.
 */
public interface ExchangeRateRepository {
    
    /**
     * Saves an exchange rate data point.
     * 
     * @param exchangeRate The exchange rate to save
     * @return The saved exchange rate
     */
    ExchangeRate save(ExchangeRate exchangeRate);
    
    /**
     * Saves multiple exchange rate data points.
     * 
     * @param exchangeRates The list of exchange rates to save
     * @return The saved exchange rates
     */
    List<ExchangeRate> saveAll(List<ExchangeRate> exchangeRates);
    
    /**
     * Finds the most recent exchange rate.
     * 
     * @return An Optional containing the most recent exchange rate, or empty if none exists
     */
    Optional<ExchangeRate> findMostRecent();
    
    /**
     * Finds exchange rates between the specified start and end times.
     * 
     * @param startTime The start time (inclusive)
     * @param endTime The end time (inclusive)
     * @return A list of exchange rates within the specified time range
     */
    List<ExchangeRate> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Finds exchange rates from the specified time until now.
     * 
     * @param startTime The start time (inclusive)
     * @return A list of exchange rates from the specified time until now
     */
    List<ExchangeRate> findByTimestampAfter(LocalDateTime startTime);
    
    /**
     * Clears all exchange rate data.
     */
    void deleteAll();
}