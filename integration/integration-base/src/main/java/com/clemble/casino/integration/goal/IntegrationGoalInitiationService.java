package com.clemble.casino.integration.goal;

import com.clemble.casino.bet.Bid;
import com.clemble.casino.goal.lifecycle.initiation.GoalInitiation;
import com.clemble.casino.goal.construction.controller.GoalInitiationServiceController;
import com.clemble.casino.goal.lifecycle.initiation.service.GoalInitiationService;
import com.clemble.casino.goal.lifecycle.management.GoalRole;

import java.util.Collection;

/**
 * Created by mavarazy on 9/15/14.
 */
public class IntegrationGoalInitiationService implements GoalInitiationService {

    final private String player;
    final private GoalInitiationServiceController initiationService;

    public IntegrationGoalInitiationService(String player, GoalInitiationServiceController initiationService) {
        this.player = player;
        this.initiationService = initiationService;
    }

    @Override
    public Collection<GoalInitiation> getPending() {
        return initiationService.getPending(player);
    }

    @Override
    public GoalInitiation get(String key) {
        return initiationService.get(key);
    }

    @Override
    public GoalInitiation confirm(String key) {
        return initiationService.confirm(player, key);
    }

}
