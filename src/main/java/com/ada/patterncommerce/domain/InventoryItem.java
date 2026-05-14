package com.ada.patterncommerce.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class InventoryItem {
    private final String sku;
    private final AtomicInteger available;

    public InventoryItem(String sku, int initialQuantity) {
        this.sku = sku;
        this.available = new AtomicInteger(initialQuantity);
    }

    public String sku() {
        return sku;
    }

    public int available() {
        return available.get();
    }

    public void restock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Restock quantity must be positive");
        }
        available.addAndGet(quantity);
    }

    public boolean reserve(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Reservation quantity must be positive");
        }
        while (true) {
            int current = available.get();
            if (current < quantity) {
                return false;
            }
            if (available.compareAndSet(current, current - quantity)) {
                return true;
            }
        }
    }
}
