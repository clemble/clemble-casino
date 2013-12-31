package com.clemble.casino.server.game.aspect;

import org.springframework.core.Ordered;

import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.event.Event;

/**
 * Created by mavarazy on 24/12/13.
 */
abstract public class BasicGameAspect<T extends Event> implements GameAspect<T> {

    final private EventSelector selector;

    public BasicGameAspect(EventSelector selector) {
        this.selector = selector;
    }

    @Override
    public void onEvent(T event) {
        if (selector.filter(event))
            doEvent(event);
    }

    abstract public void doEvent(T event);

    @Override
    public EventSelector getSelector() {
        return selector;
    }

}
