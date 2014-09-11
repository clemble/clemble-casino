package com.clemble.casino.goal.construction.controller;

import com.clemble.casino.construction.Construction;
import com.clemble.casino.goal.configuration.GoalConfiguration;
import com.clemble.casino.goal.construction.GoalConstructionRequest;
import com.clemble.casino.goal.construction.service.GoalConstructionService;

import java.util.Collection;

/**
 * Created by mavarazy on 9/10/14.
 */
public class GoalConstructionServiceController implements GoalConstructionService<GoalConstructionRequest> {

    final private GoalConstructionService delegate;

    public GoalConstructionServiceController(GoalConstructionService delegate) {
        this.delegate = delegate;
    }

    @Override
    public Construction<GoalConfiguration> construct(GoalConstructionRequest request) {
        return delegate.construct(request);
    }

    @Override
    public Collection<? extends Construction<GoalConfiguration>> getPending(String player) {
        return delegate.getPending(player);
    }

}
