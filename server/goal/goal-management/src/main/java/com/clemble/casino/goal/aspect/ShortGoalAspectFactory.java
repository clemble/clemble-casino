package com.clemble.casino.goal.aspect;

import com.clemble.casino.event.Event;
import com.clemble.casino.goal.lifecycle.configuration.ShortGoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.ShortGoalState;

/**
 * Created by mavarazy on 12/14/14.
 */
public interface ShortGoalAspectFactory<T extends Event> extends GoalAspectFactory<T, ShortGoalState, ShortGoalConfiguration> {

}
