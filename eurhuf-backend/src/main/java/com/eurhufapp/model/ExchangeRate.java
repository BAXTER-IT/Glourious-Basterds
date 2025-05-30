package com.eurhufapp.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an exchange rate data point for EURHUF.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {
    
    /**
     * The timestamp of the exchange rate data point.
     */
    private LocalDateTime timestamp;
    
    /**
     * The exchange rate value.
     */
    private double rate;
    
    /**
     * The change from the previous value.
     * This is optional and may be null for historical data points.
     */
    private Double change;
    
    /**
     * The percentage change from the previous value.
     * This is optional and may be null for historical data points.
     */
    private Double changePercent;
}