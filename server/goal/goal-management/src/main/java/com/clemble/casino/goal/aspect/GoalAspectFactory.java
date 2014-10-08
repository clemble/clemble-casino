package com.clemble.casino.goal.aspect;

import com.clemble.casino.event.Event;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.server.aspect.ClembleAspectFactory;

/**
 * Created by mavarazy on 10/8/14.
 */
public interface GoalAspectFactory<T extends Event> extends ClembleAspectFactory<T, GoalConfiguration, GoalState> {

}
