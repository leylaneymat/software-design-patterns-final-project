package com.ada.patterncommerce.application;

import java.math.BigDecimal;

public record ProductView(String sku, String name, BigDecimal price, int available) {
}
