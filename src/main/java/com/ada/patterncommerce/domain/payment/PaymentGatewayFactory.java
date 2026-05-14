package com.ada.patterncommerce.domain.payment;

public interface PaymentGatewayFactory {
    PaymentGateway create(PaymentMethod method);
}
