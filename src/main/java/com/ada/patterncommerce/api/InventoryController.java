package com.ada.patterncommerce.api;

import com.ada.patterncommerce.domain.InventoryItem;
import com.ada.patterncommerce.infrastructure.repository.InventoryRepository;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryRepository inventory;

    public InventoryController(InventoryRepository inventory) {
        this.inventory = inventory;
    }

    @PostMapping("/{sku}/restock")
    InventoryResponse restock(@PathVariable String sku, @RequestParam @Positive int quantity) {
        InventoryItem item = inventory.restock(sku, quantity);
        return new InventoryResponse(item.sku(), item.available());
    }

    record InventoryResponse(String sku, int available) {
    }
}
