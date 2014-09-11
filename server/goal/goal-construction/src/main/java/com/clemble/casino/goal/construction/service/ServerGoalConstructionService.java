package com.clemble.casino.goal.construction.service;

import com.clemble.casino.construction.Construction;
import com.clemble.casino.construction.ConstructionState;
import com.clemble.casino.goal.configuration.GoalConfiguration;
import com.clemble.casino.goal.construction.GoalConstructionRequest;
import com.clemble.casino.goal.construction.repository.GoalConstructionRepository;

import java.util.Collection;

/**
 * Created by mavarazy on 9/10/14.
 */
public class ServerGoalConstructionService implements GoalConstructionService<GoalConstructionRequest> {

    final private GoalConstructionRepository constructionRepository;
    final private SelfGoalConstructionService selfGoalConstructionService;

    public ServerGoalConstructionService(SelfGoalConstructionService selfGoalConstructionService, GoalConstructionRepository constructionRepository) {
        this.constructionRepository = constructionRepository;
        this.selfGoalConstructionService = selfGoalConstructionService;
    }

    @Override
    public Construction<GoalConfiguration> construct(GoalConstructionRequest request) {
        return selfGoalConstructionService.construct(request);
    }

    @Override
    public Collection<? extends Construction<GoalConfiguration>> getPending(String player) {
        return constructionRepository.findByPlayerAndState(player, ConstructionState.pending);
    }

}
