package com.musicstream.shared;

import java.time.Instant;

/**
 * Marker contract for something noteworthy that happened in the domain.
 * Lives in the Shared Kernel because every context raises events and they may
 * cross context boundaries.
 */
public interface DomainEvent {

    /** The moment, in domain time, at which the event occurred. */
    Instant occurredOn();
}
