package com.clemble.casino.goal.construction.controller;

import com.clemble.casino.goal.lifecycle.construction.GoalConstruction;
import com.clemble.casino.goal.lifecycle.construction.GoalConstructionRequest;
import com.clemble.casino.goal.lifecycle.construction.service.GoalConstructionService;
import com.clemble.casino.goal.construction.service.ServerGoalConstructionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com.clemble.casino.goal.GoalWebMapping.*;
import static com.clemble.casino.WebMapping.PRODUCES;

/**
 * Created by mavarazy on 9/10/14.
 */
@RestController
public class GoalConstructionServiceController implements GoalConstructionService {

    final private ServerGoalConstructionService delegate;

    public GoalConstructionServiceController(ServerGoalConstructionService delegate) {
        this.delegate = delegate;
    }

    @Override
    public GoalConstruction construct(GoalConstructionRequest request) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.POST, value = GOAL_CONSTRUCTION, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public GoalConstruction construct(@CookieValue("player") String player, @RequestBody GoalConstructionRequest request) {
        return delegate.construct(player, request);
    }


    @Override
    @RequestMapping(method = RequestMethod.GET, value = GOAL_CONSTRUCTION_PENDING, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<GoalConstruction> getPending(@CookieValue("player") String player) {
        return delegate.getPending(player);
    }

}
