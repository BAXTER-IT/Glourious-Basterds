package com.eurhufapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.eurhufapp.config.CacheConfig;

/**
 * Service for managing the application's cache.
 * Provides methods for clearing and managing cache entries.
 */
@Service
public class CacheService {
    
    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);
    
    private final CacheManager cacheManager;
    
    /**
     * Constructs a new CacheService with the specified CacheManager.
     * 
     * @param cacheManager The cache manager to use
     */
    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    
    /**
     * Clears all caches used by the application.
     */
    public void clearAllCaches() {
        logger.info("Clearing all caches");
        clearCache(CacheConfig.CURRENT_RATE_CACHE);
        clearCache(CacheConfig.HISTORICAL_DATA_CACHE);
        clearCache(CacheConfig.RANGE_DATA_CACHE);
    }
    
    /**
     * Clears the cache with the specified name.
     * 
     * @param cacheName The name of the cache to clear
     */
    public void clearCache(String cacheName) {
        logger.debug("Clearing cache: {}", cacheName);
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
            logger.debug("Cache {} cleared", cacheName);
        } else {
            logger.warn("Cache {} not found", cacheName);
        }
    }
    
    /**
     * Checks if the cache with the specified name needs to be updated.
     * This is a placeholder method that always returns true for now.
     * In a real application, this would check the age of the cache entries.
     * 
     * @param cacheName The name of the cache to check
     * @return true if the cache needs to be updated, false otherwise
     */
    public boolean needsUpdate(String cacheName) {
        // In a real application, this would check the age of the cache entries
        // For now, we'll always return true to ensure the cache is updated
        logger.debug("Checking if cache {} needs update", cacheName);
        return true;
    }
}