package com.ada.patterncommerce.application;

import com.ada.patterncommerce.infrastructure.supplier.SupplierClient;
import org.springframework.stereotype.Service;

@Service
public class SupplierAvailabilityService {
    private final SupplierClient supplierClient;

    public SupplierAvailabilityService(SupplierClient supplierClient) {
        this.supplierClient = supplierClient;
    }

    public boolean canFulfill(String sku, int quantity) {
        return supplierClient.isAvailable(sku, quantity);
    }
}
