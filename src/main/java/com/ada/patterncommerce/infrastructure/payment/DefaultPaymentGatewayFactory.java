package com.ada.patterncommerce.infrastructure.payment;

import com.ada.patterncommerce.domain.payment.PaymentGateway;
import com.ada.patterncommerce.domain.payment.PaymentGatewayFactory;
import com.ada.patterncommerce.domain.payment.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class DefaultPaymentGatewayFactory implements PaymentGatewayFactory {
    private final CardPaymentGateway cardGateway;
    private final WalletPaymentGateway walletGateway;

    public DefaultPaymentGatewayFactory(CardPaymentGateway cardGateway, WalletPaymentGateway walletGateway) {
        this.cardGateway = cardGateway;
        this.walletGateway = walletGateway;
    }

    @Override
    public PaymentGateway create(PaymentMethod method) {
        return switch (method) {
            case CARD -> cardGateway;
            case WALLET -> walletGateway;
        };
    }
}
