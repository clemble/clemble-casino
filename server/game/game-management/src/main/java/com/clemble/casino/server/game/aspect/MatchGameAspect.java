package com.clemble.casino.server.game.aspect;

import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.event.Event;

/**
 * Created by mavarazy on 11/19/14.
 */
abstract public class MatchGameAspect<T extends Event> extends GameAspect<T> {

    public MatchGameAspect(EventSelector selector) {
        super(selector);
    }

}
