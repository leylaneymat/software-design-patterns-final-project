package com.ada.patterncommerce.domain;

import java.math.BigDecimal;

public record Product(String sku, String name, BigDecimal price) {
    public Product {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU is required");
        }
        if (price == null || price.signum() <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }
}
