package com.clemble.casino.integration.event;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.event.Event;

public class EventAccumulator implements EventListener<Event> {

    final private LinkedBlockingQueue<Event> events = new LinkedBlockingQueue<>();

    @Override
    public void onEvent(Event event) {
        // Step 1. Adding event to the Queue
        events.add(event);
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> T poll() {
        return (T) events.poll();
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> T poll(int timeout, TimeUnit unit) {
        try {
            return (T) events.poll(timeout, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int size() {
        return events.size();
    }
}
