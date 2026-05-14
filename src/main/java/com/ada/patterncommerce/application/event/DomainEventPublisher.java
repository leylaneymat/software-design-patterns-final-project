package com.ada.patterncommerce.application.event;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DomainEventPublisher {
    private final List<DomainEventListener<? extends DomainEvent>> listeners;

    public DomainEventPublisher(List<DomainEventListener<? extends DomainEvent>> listeners) {
        this.listeners = listeners;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void publish(DomainEvent event) {
        listeners.stream()
                .filter(listener -> listener.supports(event))
                .forEach(listener -> ((DomainEventListener) listener).onEvent(event));
    }
}
