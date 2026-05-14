package com.ada.patterncommerce.infrastructure.repository;

import com.ada.patterncommerce.domain.InventoryItem;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InventoryRepository {
    private final ConcurrentHashMap<String, InventoryItem> inventory = new ConcurrentHashMap<>();

    public InventoryItem restock(String sku, int quantity) {
        return inventory.compute(sku, (key, existing) -> {
            if (existing == null) {
                return new InventoryItem(sku, quantity);
            }
            existing.restock(quantity);
            return existing;
        });
    }

    public Optional<InventoryItem> findBySku(String sku) {
        return Optional.ofNullable(inventory.get(sku));
    }

    public Collection<InventoryItem> findAll() {
        return inventory.values();
    }
}
