package com.crypto.orderbook.model;

import java.util.List;

public class Orderbook {
    private List<OrderbookEntry> bids;
    private List<OrderbookEntry> asks;
    private long lastUpdateId;
    private long timestamp;

    public Orderbook() {
    }

    public Orderbook(List<OrderbookEntry> bids, List<OrderbookEntry> asks, long lastUpdateId, long timestamp) {
        this.bids = bids;
        this.asks = asks;
        this.lastUpdateId = lastUpdateId;
        this.timestamp = timestamp;
    }

    public List<OrderbookEntry> getBids() {
        return bids;
    }

    public void setBids(List<OrderbookEntry> bids) {
        this.bids = bids;
    }

    public List<OrderbookEntry> getAsks() {
        return asks;
    }

    public void setAsks(List<OrderbookEntry> asks) {
        this.asks = asks;
    }

    public long getLastUpdateId() {
        return lastUpdateId;
    }

    public void setLastUpdateId(long lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Orderbook{" +
                "bids=" + bids +
                ", asks=" + asks +
                ", lastUpdateId=" + lastUpdateId +
                ", timestamp=" + timestamp +
                '}';
    }
}