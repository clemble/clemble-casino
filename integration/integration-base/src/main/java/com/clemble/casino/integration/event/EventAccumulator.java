package com.clemble.casino.integration.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.clemble.casino.client.event.EventListener;
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

    public List<T> toList(){
        return new ArrayList<>(events);
    }

    public int size() {
        return events.size();
    }
}
