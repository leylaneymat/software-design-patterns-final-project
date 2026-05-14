package com.ada.patterncommerce.application.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderPaidEvent(UUID orderId, BigDecimal total, Instant occurredAt) implements DomainEvent {
}
