package com.crypto.orderbook.model;

import java.math.BigDecimal;

public class OrderbookEntry {
    private BigDecimal price;
    private BigDecimal quantity;
    private boolean isUserOrder;

    public OrderbookEntry() {
    }

    public OrderbookEntry(BigDecimal price, BigDecimal quantity) {
        this.price = price;
        this.quantity = quantity;
        this.isUserOrder = false;
    }

    public OrderbookEntry(BigDecimal price, BigDecimal quantity, boolean isUserOrder) {
        this.price = price;
        this.quantity = quantity;
        this.isUserOrder = isUserOrder;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public boolean isUserOrder() {
        return isUserOrder;
    }

    public void setUserOrder(boolean userOrder) {
        isUserOrder = userOrder;
    }

    @Override
    public String toString() {
        return "OrderbookEntry{" +
                "price=" + price +
                ", quantity=" + quantity +
                ", isUserOrder=" + isUserOrder +
                '}';
    }
}