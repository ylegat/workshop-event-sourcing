package com.github.ylegat.workshop.infrastructure;

import static java.lang.String.format;
import java.util.ArrayList;
import java.util.List;
import com.github.ylegat.workshop.domain.common.ConflictingEventException;
import com.github.ylegat.workshop.domain.common.Event;
import com.github.ylegat.workshop.domain.common.EventBus;
import com.github.ylegat.workshop.domain.common.EventStore;
import com.google.common.collect.ArrayListMultimap;

public class InMemoryEventStore extends EventStore {

    private final Object monitor = new Object();

    private final ArrayListMultimap<String, Event> store = ArrayListMultimap.create();

    public InMemoryEventStore(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    public List<Event> save(int aggregateVersion, List<Event> events) {
        synchronized (monitor) {
            if (events.isEmpty()) {
                return events;
            }

            String aggregateId = events.get(0).aggregateId;
            List<Event> currentEvents = store.get(aggregateId);
            if (currentEvents.size() != aggregateVersion) {
                throw new ConflictingEventException(format("conflict when saving events for aggregate '%s' : version %s already exist",
                                                           aggregateId,
                                                           aggregateVersion));
            }

            currentEvents.addAll(events);
            dispatchToEventBus(events);
            return events;
        }
    }

    @Override
    public List<Event> load(String bankAccountId, int fromAggregateVersion) {
        synchronized (monitor) {
            return new ArrayList<>(store.get(bankAccountId));
        }
    }

    @Override
    public void clear() {
        synchronized (monitor) {
            store.clear();
        }
    }
}
