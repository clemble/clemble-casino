package com.clemble.casino.integration.goal;

import com.clemble.casino.goal.construction.GoalInitiation;
import com.clemble.casino.goal.construction.controller.GoalInitiationServiceController;
import com.clemble.casino.goal.construction.service.GoalInitiationService;

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

}
