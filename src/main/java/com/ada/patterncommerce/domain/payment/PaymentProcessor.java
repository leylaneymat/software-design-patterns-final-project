package com.ada.patterncommerce.domain.payment;

import com.ada.patterncommerce.domain.order.Order;

public interface PaymentProcessor {
    PaymentReceipt pay(Order order, PaymentMethod method);
}
