package com.clemble.casino.goal.aspect;

import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.event.Event;
import com.clemble.casino.server.aspect.ClembleAspect;

/**
 * Created by mavarazy on 10/8/14.
 */
abstract public class GoalAspect<T extends Event> extends ClembleAspect<T> {

    public GoalAspect(EventSelector selector) {
        super(selector);
    }

    abstract public void doEvent(T event);

}
