package com.ada.patterncommerce.domain.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private final UUID id;
    private final String customerId;
    private final List<OrderLine> lines;
    private final BigDecimal total;
    private final Instant createdAt;
    private OrderStatus status;

    private Order(Builder builder) {
        this.id = builder.id;
        this.customerId = builder.customerId;
        this.lines = List.copyOf(builder.lines);
        this.total = builder.total;
        this.createdAt = builder.createdAt;
        this.status = builder.status;
    }

    public static Builder builder(String customerId) {
        return new Builder(customerId);
    }

    public UUID id() {
        return id;
    }

    public String customerId() {
        return customerId;
    }

    public List<OrderLine> lines() {
        return lines;
    }

    public BigDecimal total() {
        return total;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public OrderStatus status() {
        return status;
    }

    public void markReserved() {
        status = OrderStatus.RESERVED;
    }

    public void markPaid() {
        status = OrderStatus.PAID;
    }

    public void reject() {
        status = OrderStatus.REJECTED;
    }

    public static final class Builder {
        private final UUID id = UUID.randomUUID();
        private final String customerId;
        private final List<OrderLine> lines = new ArrayList<>();
        private BigDecimal total = BigDecimal.ZERO;
        private Instant createdAt = Instant.now();
        private OrderStatus status = OrderStatus.CREATED;

        private Builder(String customerId) {
            if (customerId == null || customerId.isBlank()) {
                throw new IllegalArgumentException("Customer id is required");
            }
            this.customerId = customerId;
        }

        public Builder addLine(OrderLine line) {
            lines.add(line);
            total = total.add(line.subtotal());
            return this;
        }

        public Builder total(BigDecimal total) {
            this.total = total;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Order build() {
            if (lines.isEmpty()) {
                throw new IllegalStateException("Order requires at least one line");
            }
            return new Order(this);
        }
    }
}
