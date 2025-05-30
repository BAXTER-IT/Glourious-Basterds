package com.eurhufapp.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.eurhufapp.model.ExchangeRate;

/**
 * In-memory implementation of the ExchangeRateRepository interface.
 * Uses a ConcurrentHashMap to store exchange rate data for thread safety.
 */
@Repository
public class InMemoryExchangeRateRepository implements ExchangeRateRepository {
    
    /**
     * In-memory storage for exchange rate data.
     * The key is the timestamp of the exchange rate, and the value is the exchange rate itself.
     */
    private final ConcurrentHashMap<LocalDateTime, ExchangeRate> storage = new ConcurrentHashMap<>();
    
    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) {
        storage.put(exchangeRate.getTimestamp(), exchangeRate);
        return exchangeRate;
    }
    
    @Override
    public List<ExchangeRate> saveAll(List<ExchangeRate> exchangeRates) {
        exchangeRates.forEach(rate -> storage.put(rate.getTimestamp(), rate));
        return exchangeRates;
    }
    
    @Override
    public Optional<ExchangeRate> findMostRecent() {
        return storage.values().stream()
                .max(Comparator.comparing(ExchangeRate::getTimestamp));
    }
    
    @Override
    public List<ExchangeRate> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return storage.values().stream()
                .filter(rate -> !rate.getTimestamp().isBefore(startTime) && !rate.getTimestamp().isAfter(endTime))
                .sorted(Comparator.comparing(ExchangeRate::getTimestamp))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ExchangeRate> findByTimestampAfter(LocalDateTime startTime) {
        return storage.values().stream()
                .filter(rate -> !rate.getTimestamp().isBefore(startTime))
                .sorted(Comparator.comparing(ExchangeRate::getTimestamp))
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteAll() {
        storage.clear();
    }
    
    /**
     * Gets all exchange rates stored in the repository.
     * 
     * @return A list of all exchange rates
     */
    public List<ExchangeRate> findAll() {
        return new ArrayList<>(storage.values());
    }
}