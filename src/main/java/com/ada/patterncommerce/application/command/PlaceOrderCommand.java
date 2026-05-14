package com.ada.patterncommerce.application.command;

import com.ada.patterncommerce.api.PlaceOrderRequest;
import com.ada.patterncommerce.application.InventoryReservationService;
import com.ada.patterncommerce.application.OrderPricingService;
import com.ada.patterncommerce.application.OrderResponse;
import com.ada.patterncommerce.application.SupplierAvailabilityService;
import com.ada.patterncommerce.application.event.DomainEventPublisher;
import com.ada.patterncommerce.application.event.OrderPaidEvent;
import com.ada.patterncommerce.domain.order.Order;
import com.ada.patterncommerce.domain.order.OrderLine;
import com.ada.patterncommerce.domain.payment.PaymentProcessor;
import com.ada.patterncommerce.domain.payment.PaymentReceipt;
import com.ada.patterncommerce.infrastructure.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class PlaceOrderCommand implements OrderCommand {
    private final PlaceOrderRequest request;
    private final List<OrderLine> lines;
    private final InventoryReservationService inventoryReservation;
    private final SupplierAvailabilityService supplierAvailability;
    private final OrderPricingService pricing;
    private final PaymentProcessor paymentProcessor;
    private final OrderRepository orders;
    private final DomainEventPublisher events;

    public PlaceOrderCommand(
            PlaceOrderRequest request,
            List<OrderLine> lines,
            InventoryReservationService inventoryReservation,
            SupplierAvailabilityService supplierAvailability,
            OrderPricingService pricing,
            PaymentProcessor paymentProcessor,
            OrderRepository orders,
            DomainEventPublisher events
    ) {
        this.request = request;
        this.lines = lines;
        this.inventoryReservation = inventoryReservation;
        this.supplierAvailability = supplierAvailability;
        this.pricing = pricing;
        this.paymentProcessor = paymentProcessor;
        this.orders = orders;
        this.events = events;
    }

    @Override
    public OrderResponse execute() {
        for (OrderLine line : lines) {
            if (!supplierAvailability.canFulfill(line.sku(), line.quantity())) {
                throw new IllegalStateException("Supplier cannot fulfill " + line.sku());
            }
        }

        BigDecimal finalTotal = pricing.price(lines, request.pricingCode());
        Order.Builder builder = Order.builder(request.customerId());
        lines.forEach(builder::addLine);
        Order order = builder.total(finalTotal).build();

        inventoryReservation.reserve(lines);
        order.markReserved();

        PaymentReceipt receipt = paymentProcessor.pay(order, request.paymentMethod());
        order.markPaid();
        orders.save(order);
        events.publish(new OrderPaidEvent(order.id(), order.total(), Instant.now()));

        return OrderResponse.from(order, receipt.providerReference());
    }
}
