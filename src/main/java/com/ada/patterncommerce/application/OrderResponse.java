package com.ada.patterncommerce.application;

import com.ada.patterncommerce.domain.order.Order;
import com.ada.patterncommerce.domain.order.OrderLine;
import com.ada.patterncommerce.domain.order.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String customerId,
        List<OrderLine> lines,
        BigDecimal total,
        OrderStatus status,
        String paymentReference,
        Instant createdAt
) {
    public static OrderResponse from(Order order, String paymentReference) {
        return new OrderResponse(
                order.id(),
                order.customerId(),
                order.lines(),
                order.total(),
                order.status(),
                paymentReference,
                order.createdAt()
        );
    }
}
