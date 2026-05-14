package com.ada.patterncommerce.infrastructure.payment;

import com.ada.patterncommerce.domain.order.Order;
import com.ada.patterncommerce.domain.payment.PaymentMethod;
import com.ada.patterncommerce.domain.payment.PaymentProcessor;
import com.ada.patterncommerce.domain.payment.PaymentReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class AuditingPaymentProcessorDecorator implements PaymentProcessor {
    private static final Logger log = LoggerFactory.getLogger(AuditingPaymentProcessorDecorator.class);
    private final GatewayPaymentProcessor delegate;

    public AuditingPaymentProcessorDecorator(GatewayPaymentProcessor delegate) {
        this.delegate = delegate;
    }

    @Override
    public PaymentReceipt pay(Order order, PaymentMethod method) {
        PaymentReceipt receipt = delegate.pay(order, method);
        log.info("Payment audit: order={} method={} reference={}", order.id(), method, receipt.providerReference());
        return receipt;
    }
}
