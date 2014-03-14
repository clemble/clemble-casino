package com.clemble.casino.integration.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.event.Event;

public class EventAccumulator<T extends Event> implements EventListener<T> {

    final private LinkedBlockingQueue<T> events = new LinkedBlockingQueue<>();

    @Override
    public void onEvent(T event) {
        // Step 1. Adding event to the Queue
        events.add(event);
    }

    public T poll() {
        return events.poll();
    }

    public T poll(int timeout, TimeUnit unit) {
        try {
            return events.poll(timeout, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }

    public Event waitFor(EventSelector event) {
        return waitFor(event, 15_000);
    }

    public Event waitFor(EventSelector selector, long timeout) {
        // Step 1. Sanity check
        if (selector == null)
            selector = EventSelector.TRUE;
        // Step 2. Poll until receive event or first timeout
        Event actual = null;
        long waitExpiration = System.currentTimeMillis() + timeout;
        do {
            try {
                actual = events.poll(waitExpiration - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                actual = null;
            }
        } while (!selector.filter(actual) && waitExpiration > System.currentTimeMillis());
        // Step 3. Returning true only if actual matches polled
        return actual;
    }

    public List<T> toList() {
        return new ArrayList<>(events);
    }

    public int size() {
        return events.size();
    }
}
