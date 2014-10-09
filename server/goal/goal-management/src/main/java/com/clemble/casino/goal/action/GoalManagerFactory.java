package com.clemble.casino.goal.action;

import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.lifecycle.initiation.GoalInitiation;
import com.clemble.casino.goal.lifecycle.management.GoalContext;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.server.action.ClembleManager;

/**
 * Created by mavarazy on 10/9/14.
 */
public interface GoalManagerFactory {

    public ClembleManager<GoalEvent, ? extends GoalState> start(GoalContext parent, GoalInitiation initiation);

}
