package com.github.ylegat.workshop.domain.common;

import static java.time.Instant.now;
import java.time.Instant;

public abstract class Event {

    public final String aggregateId;

    public final Instant eventCreationDate;

    public Event(String aggregateId) {
        this.aggregateId = aggregateId;
        this.eventCreationDate = now();
    }

    public abstract void applyOn(EventListener eventListener);

    @Override
    public String toString() {
        return "Event{" +
                "aggregateId=" + aggregateId +
                ", eventCreationDate=" + eventCreationDate +
                '}';
    }
}
