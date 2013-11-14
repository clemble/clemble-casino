package com.clemble.casino.client.event;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import com.clemble.casino.ImmutablePair;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionKey;

abstract public class AbstractEventListenerTemplate implements EventListenerOperation, Closeable {

    final protected Set<Entry<EventSelector, EventListener>> eventListeners = new HashSet<>();
    final protected ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    final protected AtomicReference<Closeable> connectionCleaner = new AtomicReference<>();

    @Override
    final public EventListenerController subscribe(EventListener listener) {
        return subscribe((EventSelector) null, listener);
    }

    @Override
    final public EventListenerController subscribe(GameSessionKey sessionKey, EventListener listener) {
        return subscribe(new SessionEventSelector(sessionKey), listener);
    }

    @Override
    final public EventListenerController subscribe(EventSelector selector, EventListener listener) {
        // Step 1. Generating event listener to use
        final ImmutablePair<EventSelector, EventListener> listenerPair = new ImmutablePair<EventSelector, EventListener>(selector, listener);
        // Step 2. Adding eventListener to the list
        this.eventListeners.add(listenerPair);
        // Step 3. Adding eventListener controller
        return new EventListenerController() {

            @Override
            public void close() {
                eventListeners.remove(listenerPair);
            }

        };
    }

    protected void update(Event event) {
        // Step 1. Checking event value
        if (event == null)
            return;
        // Step 2. Checking and notifying Event selectors
        for (Entry<EventSelector, EventListener> listener : eventListeners) {
            EventSelector selector = listener.getKey();
            if (selector == null || selector.filter(event))
                listener.getValue().onEvent(event);
        }
    }

    public void close() {
        if (connectionCleaner.get() != null) {
            try {
                connectionCleaner.get().close();
            } catch (IOException e) {
            }
        }
        executor.shutdown();
    }

}
