package com.clemble.casino.goal.construction.service;

import com.clemble.casino.goal.lifecycle.construction.service.GoalConstructionService;
import com.clemble.casino.lifecycle.construction.ConstructionState;
import com.clemble.casino.goal.lifecycle.construction.GoalConstruction;
import com.clemble.casino.goal.lifecycle.construction.GoalConstructionRequest;
import com.clemble.casino.goal.construction.repository.GoalConstructionRepository;

import java.util.Collection;

/**
 * Created by mavarazy on 9/10/14.
 */
public class ServerGoalConstructionService implements GoalConstructionService {

    final private GoalConstructionRepository constructionRepository;
    final private SelfGoalConstructionService selfGoalConstructionService;

    public ServerGoalConstructionService(SelfGoalConstructionService selfGoalConstructionService, GoalConstructionRepository constructionRepository) {
        this.constructionRepository = constructionRepository;
        this.selfGoalConstructionService = selfGoalConstructionService;
    }

    @Override
    public GoalConstruction construct(GoalConstructionRequest request) {
        throw new UnsupportedOperationException();
    }

    public GoalConstruction construct(String player, GoalConstructionRequest request) {
        return selfGoalConstructionService.construct(player, request);
    }

    @Override
    public Collection<GoalConstruction> getPending(String player) {
        return constructionRepository.findByPlayerAndState(player, ConstructionState.pending);
    }

}
