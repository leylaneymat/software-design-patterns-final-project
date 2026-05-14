package com.ada.patterncommerce.application.event;

public interface DomainEventListener<T extends DomainEvent> {
    boolean supports(DomainEvent event);

    void onEvent(T event);
}
