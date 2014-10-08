package com.clemble.casino.goal.construction.controller;

import com.clemble.casino.goal.lifecycle.initiation.GoalInitiation;
import com.clemble.casino.goal.lifecycle.initiation.service.GoalInitiationService;
import com.clemble.casino.goal.lifecycle.construction.service.ServerGoalInitiationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com.clemble.casino.goal.GoalWebMapping.GOAL_INITIATION_PENDING;
import static com.clemble.casino.WebMapping.PRODUCES;

/**
 * Created by mavarazy on 9/13/14.
 */
@RestController
public class GoalInitiationServiceController implements GoalInitiationService {

    final private ServerGoalInitiationService initiationService;

    public GoalInitiationServiceController(ServerGoalInitiationService initiationService) {
        this.initiationService = initiationService;
    }

    @Override
    public Collection<GoalInitiation> getPending() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = GOAL_INITIATION_PENDING, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<GoalInitiation> getPending(@CookieValue("player") String player) {
        return initiationService.getPending(player);
    }

}
