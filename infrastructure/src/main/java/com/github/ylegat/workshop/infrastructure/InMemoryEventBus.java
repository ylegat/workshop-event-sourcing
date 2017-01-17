package com.github.ylegat.workshop.infrastructure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.github.ylegat.workshop.domain.common.Event;
import com.github.ylegat.workshop.domain.common.EventBus;
import com.github.ylegat.workshop.domain.common.EventListener;

public class InMemoryEventBus implements EventBus {

    private final Set<EventListener> listeners = new HashSet<>();

    @Override
    public void register(EventListener eventListener) {
        listeners.add(eventListener);
    }

    @Override
    public void push(List<Event> events) {
        events.forEach(event -> listeners.forEach(event::applyOn));
    }

    @Override
    public void clear() {
        listeners.clear();
    }
}
