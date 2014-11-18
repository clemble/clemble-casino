package com.clemble.casino.integration.goal;

import com.clemble.casino.goal.controller.GoalActionServiceController;
import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.service.GoalActionService;
import com.clemble.casino.lifecycle.management.event.action.Action;

import java.util.List;

/**
 * Created by mavarazy on 17/10/14.
 */
public class IntegrationGoalActionService implements GoalActionService {

    final private String player;
    final private GoalActionServiceController actionService;

    public IntegrationGoalActionService(String player, GoalActionServiceController actionService) {
        this.player = player;
        this.actionService = actionService;
    }

    @Override
    public List<GoalState> myActive() {
        return actionService.myActive(player);
    }

    @Override
    public List<GoalState> getActive(String player) {
        return actionService.getActive(player);
    }

    @Override
    public GoalEvent process(String goalKey, Action action) {
        return actionService.process(goalKey, player, action);
    }

    @Override
    public GoalState getState(String goalKey) {
        return actionService.getState(goalKey);
    }

}
