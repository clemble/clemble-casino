package com.clemble.casino.goal.aspect;

import com.clemble.casino.event.Event;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;

/**
 * Created by mavarazy on 12/14/14.
 */
public interface GenericGoalAspectFactory<T extends Event> extends GoalAspectFactory<T, GoalState, GoalConfiguration> {
}
