package com.clemble.casino.client.event;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import com.clemble.casino.ImmutablePair;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionKey;

abstract public class AbstractEventListenerTemplate implements EventListenerOperations, Closeable {

    /**
     * Generated 29/11/13
     */
    private static final long serialVersionUID = 6661393998011634474L;

    final private String player;

    final protected Map<String, Set<Entry<EventSelector, EventListener>>> eventListeners = new HashMap<String, Set<Entry<EventSelector, EventListener>>>();
    final protected ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    final protected AtomicReference<Closeable> connectionCleaner = new AtomicReference<>();

    public AbstractEventListenerTemplate(String player){
        this.player = checkNotNull(player);
        this.eventListeners.put(player, new HashSet<Entry<EventSelector, EventListener>>());
    }

    @Override
    public String getPlayer() {
        return player;
    }

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
        this.eventListeners.get(player).add(listenerPair);
        // Step 3. Adding eventListener controller
        return new EventListenerController() {
            @Override
            public void close() {
                eventListeners.remove(listenerPair);
            }
        };
    }

    @Override
    public EventListenerController subscribe(String channel, EventListener listener) {
        return subscribe(channel, null, listener);
    }

    @Override
    final public EventListenerController subscribe(String channel, EventSelector selector, EventListener listener) {
        if(!this.eventListeners.containsKey(channel)) {
            this.eventListeners.put(channel, new HashSet<Entry<EventSelector, EventListener>>());
            subscribe(channel);
        }
        // Step 1. Generating event listener to use
        final ImmutablePair<EventSelector, EventListener> listenerPair = new ImmutablePair<EventSelector, EventListener>(selector, listener);
        // Step 2. Adding eventListener to the list
        this.eventListeners.get(channel).add(listenerPair);
        // Step 3. Adding eventListener controller
        return new EventListenerController() {
            @Override
            public void close() {
                eventListeners.remove(listenerPair);
            }
        };
    }

    protected void refreshSubscription(){
        for(String channel: eventListeners.keySet())
            subscribe(channel);
    }

    abstract public void subscribe(String channel);

    protected void update(String channel, Event event) {
        // Step 1. Checking event value
        if (event == null)
            return;
        // Step 2. Checking and notifying Event selectors
        for (Entry<EventSelector, EventListener> listener : eventListeners.get(channel)) {
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
