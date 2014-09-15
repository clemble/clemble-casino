package com.clemble.casino.goal.construction.controller;

import com.clemble.casino.construction.Construction;
import com.clemble.casino.goal.configuration.GoalConfiguration;
import com.clemble.casino.goal.construction.GoalConstruction;
import com.clemble.casino.goal.construction.GoalConstructionRequest;
import com.clemble.casino.goal.construction.service.GoalConstructionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.clemble.casino.goal.GoalWebMapping.*;
import static com.clemble.casino.web.mapping.WebMapping.PRODUCES;

/**
 * Created by mavarazy on 9/10/14.
 */
@RestController
public class GoalConstructionServiceController implements GoalConstructionService {

    final private GoalConstructionService delegate;

    public GoalConstructionServiceController(GoalConstructionService delegate) {
        this.delegate = delegate;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = GOAL_CONSTRUCTION, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public GoalConstruction construct(GoalConstructionRequest request) {
        return delegate.construct(request);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GOAL_CONSTRUCTION_PENDING, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<GoalConstruction> getPending(String player) {
        return delegate.getPending(player);
    }

}
