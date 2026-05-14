package com.ada.patterncommerce.infrastructure.supplier;

import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class MockSupplierApi {
    public SupplierAvailabilityResponse checkStock(String externalProductCode, int units) {
        if (ThreadLocalRandom.current().nextInt(10) == 0) {
            throw new IllegalStateException("Mock supplier timeout");
        }
        return new SupplierAvailabilityResponse(externalProductCode, units <= 100);
    }
}
