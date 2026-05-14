package com.ada.patterncommerce.application.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderAuditObserver implements DomainEventListener<OrderPaidEvent> {
    private static final Logger log = LoggerFactory.getLogger(OrderAuditObserver.class);

    @Override
    public boolean supports(DomainEvent event) {
        return event instanceof OrderPaidEvent;
    }

    @Override
    public void onEvent(OrderPaidEvent event) {
        log.info("Order paid observer: order={} total={}", event.orderId(), event.total());
    }
}
