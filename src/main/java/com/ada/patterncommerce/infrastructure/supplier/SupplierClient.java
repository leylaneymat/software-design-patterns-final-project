package com.ada.patterncommerce.infrastructure.supplier;

public interface SupplierClient {
    boolean isAvailable(String sku, int quantity);
}
