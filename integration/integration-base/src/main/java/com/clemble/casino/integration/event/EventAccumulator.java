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

    public boolean waitFor(Event event) {
        return waitFor(event, 15_000);
    }

    
    public boolean waitFor(Event expected, long timeout) {
        // Step 1. Sanity check
        if(expected == null)
            return true;
        // Step 2. Poll until receive event or first timeout
        Event actual = null;
        do {
            try {
                actual = events.poll(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                actual = null;
            }
        } while(!expected.equals(actual) && actual != null);
        // Step 3. Returning true only if actual matches polled
        return expected.equals(actual);
    }

    public List<T> toList(){
        return new ArrayList<>(events);
    }

    public int size() {
        return events.size();
    }
}
