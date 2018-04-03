package com.github.ylegat.workshop.infrastructure;

import com.github.ylegat.workshop.domain.common.Event;
import com.github.ylegat.workshop.domain.common.EventBus;
import com.github.ylegat.workshop.domain.common.EventListener;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

public class InMemoryEventBus implements EventBus {

    private final Set<EventListener> listeners = new HashSet<>();

    private final ScheduledExecutorService executorService;

    public InMemoryEventBus() {
        executorService = newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("event-bus-%s").build());
    }

    @Override
    public void register(EventListener eventListener) {
        listeners.add(eventListener);
    }

    @Override
    public void push(List<Event> events) {
        executorService.submit(() -> events.forEach(event -> listeners.forEach(event::applyOn)));
    }

    @Override
    public void clear() {
        listeners.clear();
    }
}
