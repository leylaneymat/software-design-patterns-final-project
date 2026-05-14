package com.ada.patterncommerce.application;

import com.ada.patterncommerce.domain.InventoryItem;
import com.ada.patterncommerce.domain.order.OrderLine;
import com.ada.patterncommerce.infrastructure.repository.InventoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class InventoryReservationService {
    private final InventoryRepository inventory;

    public InventoryReservationService(InventoryRepository inventory) {
        this.inventory = inventory;
    }

    public void reserve(List<OrderLine> lines) {
        for (OrderLine line : lines) {
            InventoryItem item = inventory.findBySku(line.sku())
                    .orElseThrow(() -> new IllegalArgumentException("Inventory not found: " + line.sku()));
            if (!item.reserve(line.quantity())) {
                throw new IllegalStateException("Insufficient stock for " + line.sku());
            }
        }
    }
}
