package com.eurhufapp.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a collection of exchange rate data points over time.
 * Used for returning historical data and time range data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSeriesData {
    
    /**
     * The list of exchange rate data points.
     */
    private List<ExchangeRate> data;
    
    /**
     * Metadata about the time series data.
     */
    private TimeSeriesMeta meta;
    
    /**
     * Metadata about the time series data.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSeriesMeta {
        
        /**
         * The interval of the data points (e.g., "1h", "1d", "1w").
         */
        private String interval;
        
        /**
         * The number of data points in the time series.
         */
        private int count;
    }
}