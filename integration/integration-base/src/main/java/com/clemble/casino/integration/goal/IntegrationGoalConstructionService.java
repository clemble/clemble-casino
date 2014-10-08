package com.clemble.casino.integration.goal;

import com.clemble.casino.goal.lifecycle.construction.GoalConstruction;
import com.clemble.casino.goal.lifecycle.construction.GoalConstructionRequest;
import com.clemble.casino.goal.construction.controller.GoalConstructionServiceController;
import com.clemble.casino.goal.lifecycle.construction.service.GoalConstructionService;

import java.util.Collection;

/**
 * Created by mavarazy on 9/15/14.
 */
public class IntegrationGoalConstructionService implements GoalConstructionService {

    final private String player;
    final private GoalConstructionServiceController constructionService;

    public IntegrationGoalConstructionService(String player, GoalConstructionServiceController constructionService) {
        this.player = player;
        this.constructionService = constructionService;
    }

    @Override
    public GoalConstruction construct(GoalConstructionRequest request) {
        return constructionService.construct(request);
    }

    @Override
    public Collection<GoalConstruction> getPending(String player) {
        return constructionService.getPending(player);
    }
}
