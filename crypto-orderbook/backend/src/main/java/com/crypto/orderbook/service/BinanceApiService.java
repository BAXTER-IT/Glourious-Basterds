package com.crypto.orderbook.service;

import com.crypto.orderbook.model.Orderbook;
import com.crypto.orderbook.model.OrderbookEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BinanceApiService {
    private static final Logger logger = LoggerFactory.getLogger(BinanceApiService.class);
    
    private final RestTemplate restTemplate;
    
    @Value("${binance.api.base-url}")
    private String baseUrl;
    
    @Value("${binance.api.depth-endpoint}")
    private String depthEndpoint;
    
    @Value("${binance.api.default-symbol}")
    private String defaultSymbol;
    
    @Value("${binance.api.default-limit}")
    private int defaultLimit;
    
    private Orderbook latestOrderbook;
    
    public BinanceApiService() {
        this.restTemplate = new RestTemplate();
        this.latestOrderbook = new Orderbook(new ArrayList<>(), new ArrayList<>(), 0, 0);
    }
    
    @Scheduled(fixedRate = 10000)
    public void fetchOrderbook() {
        try {
            String url = baseUrl + depthEndpoint + "?symbol=" + defaultSymbol + "&limit=" + defaultLimit;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null) {
                // The Binance API response has different field names than what we expected
                long lastUpdateId = response.containsKey("lastUpdateId")
                    ? ((Number) response.get("lastUpdateId")).longValue()
                    : System.currentTimeMillis();
                long timestamp = System.currentTimeMillis();
                
                List<OrderbookEntry> bids = parseOrderbookEntries((List<List<String>>) response.get("bids"));
                List<OrderbookEntry> asks = parseOrderbookEntries((List<List<String>>) response.get("asks"));
                
                latestOrderbook = new Orderbook(bids, asks, lastUpdateId, timestamp);
                logger.debug("Fetched orderbook with {} bids and {} asks", bids.size(), asks.size());
            }
        } catch (Exception e) {
            logger.error("Error fetching orderbook: {}", e.getMessage(), e);
        }
    }
    
    private List<OrderbookEntry> parseOrderbookEntries(List<List<String>> entries) {
        List<OrderbookEntry> result = new ArrayList<>();
        
        if (entries != null) {
            for (List<String> entry : entries) {
                if (entry.size() >= 2) {
                    BigDecimal price = new BigDecimal(entry.get(0));
                    BigDecimal quantity = new BigDecimal(entry.get(1));
                    result.add(new OrderbookEntry(price, quantity));
                }
            }
        }
        
        return result;
    }
    
    public Orderbook getLatestOrderbook() {
        return latestOrderbook;
    }
    
    public Orderbook getNormalizedOrderbook(int limit) {
        List<OrderbookEntry> bids = latestOrderbook.getBids();
        List<OrderbookEntry> asks = latestOrderbook.getAsks();
        
        List<OrderbookEntry> normalizedBids = bids.size() > limit ? bids.subList(0, limit) : bids;
        List<OrderbookEntry> normalizedAsks = asks.size() > limit ? asks.subList(0, limit) : asks;
        
        return new Orderbook(normalizedBids, normalizedAsks, latestOrderbook.getLastUpdateId(), latestOrderbook.getTimestamp());
    }
}