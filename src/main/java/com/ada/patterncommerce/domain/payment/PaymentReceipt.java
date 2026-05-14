package com.ada.patterncommerce.domain.payment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentReceipt(UUID orderId, BigDecimal amount, String providerReference, Instant paidAt) {
}
