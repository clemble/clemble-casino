package com.clemble.casino.integration.goal;

import com.clemble.casino.goal.controller.GoalVictoryServiceController;
import com.clemble.casino.goal.lifecycle.management.GoalVictory;
import com.clemble.casino.goal.lifecycle.management.service.GoalVictoryService;

import java.util.List;

/**
 * Created by mavarazy on 3/14/15.
 */
public class IntegrationGoalVictoryService implements GoalVictoryService {

    final private String player;
    final private GoalVictoryServiceController victoryService;

    public IntegrationGoalVictoryService(String player, GoalVictoryServiceController victoryService) {
        this.player = player;
        this.victoryService = victoryService;
    }

    @Override
    public List<GoalVictory> listMy() {
        return victoryService.listMy(player);
    }

    @Override
    public List<GoalVictory> list(String player) {
        return victoryService.list(player);
    }

}
