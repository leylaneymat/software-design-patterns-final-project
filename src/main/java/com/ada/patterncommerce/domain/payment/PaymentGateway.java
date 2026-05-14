package com.ada.patterncommerce.domain.payment;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentGateway {
    PaymentReceipt charge(UUID orderId, BigDecimal amount);
}
