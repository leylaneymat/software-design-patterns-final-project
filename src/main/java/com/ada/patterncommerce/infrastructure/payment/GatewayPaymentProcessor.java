package com.ada.patterncommerce.infrastructure.payment;

import com.ada.patterncommerce.domain.order.Order;
import com.ada.patterncommerce.domain.payment.PaymentGatewayFactory;
import com.ada.patterncommerce.domain.payment.PaymentMethod;
import com.ada.patterncommerce.domain.payment.PaymentProcessor;
import com.ada.patterncommerce.domain.payment.PaymentReceipt;
import org.springframework.stereotype.Component;

@Component
public class GatewayPaymentProcessor implements PaymentProcessor {
    private final PaymentGatewayFactory gatewayFactory;

    public GatewayPaymentProcessor(PaymentGatewayFactory gatewayFactory) {
        this.gatewayFactory = gatewayFactory;
    }

    @Override
    public PaymentReceipt pay(Order order, PaymentMethod method) {
        return gatewayFactory.create(method).charge(order.id(), order.total());
    }
}
