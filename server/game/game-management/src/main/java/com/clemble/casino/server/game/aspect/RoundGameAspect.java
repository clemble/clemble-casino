package com.clemble.casino.server.game.aspect;

import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.event.GameEvent;

/**
 * Created by mavarazy on 11/19/14.
 */
abstract public class RoundGameAspect<T extends Event> extends GameAspect<T> {

    public RoundGameAspect(EventSelector selector) {
        super(selector);
    }

}
