package com.eurhufapp.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cache configuration for the EURHUF Chart App backend.
 * Configures caching to minimize external API calls to Yahoo Finance.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Cache names used in the application.
     */
    public static final String CURRENT_RATE_CACHE = "currentRate";
    public static final String HISTORICAL_DATA_CACHE = "historicalData";
    public static final String RANGE_DATA_CACHE = "rangeData";

    /**
     * Configures the cache manager with the required caches.
     * 
     * @return The configured cache manager
     */
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager(
                CURRENT_RATE_CACHE,
                HISTORICAL_DATA_CACHE,
                RANGE_DATA_CACHE
        );
        return cacheManager;
    }
}