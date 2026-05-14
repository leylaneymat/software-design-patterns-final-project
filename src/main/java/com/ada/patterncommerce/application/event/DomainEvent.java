package com.ada.patterncommerce.application.event;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();
}
