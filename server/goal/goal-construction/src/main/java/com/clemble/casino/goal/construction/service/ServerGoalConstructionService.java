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
public class ServerGoalConstructionService implements GoalConstructionService {

    final private GoalConstructionRepository constructionRepository;

    public ServerGoalConstructionService(GoalConstructionRepository constructionRepository) {
        this.constructionRepository = constructionRepository;
    }

    @Override
    public Construction<GoalConfiguration> construct(GoalConstructionRequest request) {
        return null;
    }

    @Override
    public Collection<? extends Construction<GoalConfiguration>> getPending(String player) {
        return constructionRepository.findByPlayerAndState(player, ConstructionState.pending);
    }
}
