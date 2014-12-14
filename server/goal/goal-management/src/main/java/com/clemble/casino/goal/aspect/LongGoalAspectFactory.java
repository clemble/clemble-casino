package com.clemble.casino.goal.aspect;

import com.clemble.casino.event.Event;
import com.clemble.casino.goal.lifecycle.configuration.LongGoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.LongGoalState;

/**
 * Created by mavarazy on 12/14/14.
 */
public interface LongGoalAspectFactory <T extends Event> extends GoalAspectFactory<T, LongGoalState, LongGoalConfiguration> {
}
