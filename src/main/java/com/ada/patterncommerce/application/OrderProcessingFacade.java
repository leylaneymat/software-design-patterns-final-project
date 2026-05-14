package com.ada.patterncommerce.application;

import com.ada.patterncommerce.api.PlaceOrderRequest;
import com.ada.patterncommerce.application.command.PlaceOrderCommand;
import com.ada.patterncommerce.application.event.DomainEventPublisher;
import com.ada.patterncommerce.domain.Product;
import com.ada.patterncommerce.domain.order.Order;
import com.ada.patterncommerce.domain.order.OrderLine;
import com.ada.patterncommerce.domain.payment.PaymentProcessor;
import com.ada.patterncommerce.infrastructure.repository.OrderRepository;
import com.ada.patterncommerce.infrastructure.repository.ProductRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessingFacade {
    private final ProductRepository products;
    private final InventoryReservationService inventoryReservation;
    private final SupplierAvailabilityService supplierAvailability;
    private final OrderPricingService pricing;
    private final PaymentProcessor paymentProcessor;
    private final OrderRepository orders;
    private final DomainEventPublisher events;

    public OrderProcessingFacade(
            ProductRepository products,
            InventoryReservationService inventoryReservation,
            SupplierAvailabilityService supplierAvailability,
            OrderPricingService pricing,
            PaymentProcessor paymentProcessor,
            OrderRepository orders,
            DomainEventPublisher events
    ) {
        this.products = products;
        this.inventoryReservation = inventoryReservation;
        this.supplierAvailability = supplierAvailability;
        this.pricing = pricing;
        this.paymentProcessor = paymentProcessor;
        this.orders = orders;
        this.events = events;
    }

    public OrderResponse placeOrder(PlaceOrderRequest request) {
        List<OrderLine> lines = request.lines().stream()
                .map(line -> {
                    Product product = products.findBySku(line.sku())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + line.sku()));
                    return new OrderLine(product.sku(), line.quantity(), product.price());
                })
                .toList();

        return new PlaceOrderCommand(
                request,
                lines,
                inventoryReservation,
                supplierAvailability,
                pricing,
                paymentProcessor,
                orders,
                events
        ).execute();
    }

    public OrderResponse findOrder(UUID id) {
        Order order = orders.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        return OrderResponse.from(order, null);
    }
}
