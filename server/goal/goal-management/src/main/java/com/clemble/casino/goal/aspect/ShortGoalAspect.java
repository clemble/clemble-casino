package com.clemble.casino.goal.aspect;

import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.event.Event;

/**
 * Created by mavarazy on 12/14/14.
 */
abstract public class ShortGoalAspect<T extends Event> extends GoalAspect<T>{

    public ShortGoalAspect(EventSelector selector) {
        super(selector);
    }

}
