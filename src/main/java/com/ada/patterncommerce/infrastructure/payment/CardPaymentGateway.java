package com.ada.patterncommerce.infrastructure.payment;

import com.ada.patterncommerce.domain.payment.PaymentGateway;
import com.ada.patterncommerce.domain.payment.PaymentReceipt;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CardPaymentGateway implements PaymentGateway {
    @Override
    public PaymentReceipt charge(UUID orderId, BigDecimal amount) {
        return new PaymentReceipt(orderId, amount, "CARD-" + UUID.randomUUID(), Instant.now());
    }
}
