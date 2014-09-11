package com.clemble.casino.goal.construction.service;

import com.clemble.casino.construction.Construction;
import com.clemble.casino.goal.configuration.GoalConfiguration;
import com.clemble.casino.goal.construction.GoalConstruction;
import com.clemble.casino.goal.construction.GoalConstructionRequest;
import com.clemble.casino.goal.construction.GoalKeyGenerator;
import com.clemble.casino.goal.construction.repository.GoalConstructionRepository;
import com.clemble.casino.goal.rule.judge.JudgeType;

import java.util.Collection;

/**
 * Created by mavarazy on 9/10/14.
 */
public class SelfGoalConstructionService implements GoalConstructionService<GoalConstructionRequest> {

    final private GoalKeyGenerator keyGenerator;
    final private GoalConstructionRepository constructionRepository;

    public SelfGoalConstructionService(GoalKeyGenerator keyGenerator, GoalConstructionRepository constructionRepository) {
        this.keyGenerator = keyGenerator;
        this.constructionRepository = constructionRepository;
    }

    @Override
    public Construction<GoalConfiguration> construct(GoalConstructionRequest request) {
        // Step 1. Checking this is appropriate request for this service
        if(request.getConfiguration().getJudgeRule().getType() != JudgeType.self)
            throw new IllegalArgumentException();
        // Step 2. Creating new GoalConfiguration
        GoalConstruction construction = request.toConstruction(keyGenerator.generate(request.getPlayer()));
        // Step 3. Saving game construction
        GoalConstruction savedConstruction =  constructionRepository.save(construction);
        // Step 4. Returning saved construction
        return savedConstruction;
    }

    @Override
    public Collection<? extends Construction<GoalConfiguration>> getPending(String player) {
        return null;
    }

}
