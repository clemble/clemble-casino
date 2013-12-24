package com.clemble.casino.server.game.aspect;

import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.event.Event;

/**
 * Created by mavarazy on 24/12/13.
 */
abstract public class BasicGameAspect implements GameAspect {

    final private EventSelector selector;

    public BasicGameAspect(EventSelector selector) {
        this.selector = selector;
    }

    @Override
    public void onEvent(Event event) {
        if(selector.filter(event))
            doEvent(event);
    }

    abstract public void doEvent(Event event);

    @Override
    public EventSelector getSelector() {
        return selector;
    }
}
