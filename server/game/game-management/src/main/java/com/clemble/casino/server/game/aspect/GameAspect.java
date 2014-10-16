package com.clemble.casino.server.game.aspect;

import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.event.Event;
import com.clemble.casino.server.aspect.ClembleAspect;

abstract public class GameAspect<T extends Event> extends ClembleAspect<T> {

    public GameAspect(EventSelector selector) {
        super(selector);
    }

}
