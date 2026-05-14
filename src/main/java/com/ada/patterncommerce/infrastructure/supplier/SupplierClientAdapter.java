package com.ada.patterncommerce.infrastructure.supplier;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;

@Component
public class SupplierClientAdapter implements SupplierClient {
    private final MockSupplierApi api;

    public SupplierClientAdapter(MockSupplierApi api) {
        this.api = api;
    }

    @Override
    @CircuitBreaker(name = "supplierAvailability", fallbackMethod = "fallbackAvailability")
    public boolean isAvailable(String sku, int quantity) {
        String externalProductCode = "EXT-" + sku;
        return api.checkStock(externalProductCode, quantity).available();
    }

    @SuppressWarnings("unused")
    public boolean fallbackAvailability(String sku, int quantity, Throwable cause) {
        return quantity <= 5;
    }
}
