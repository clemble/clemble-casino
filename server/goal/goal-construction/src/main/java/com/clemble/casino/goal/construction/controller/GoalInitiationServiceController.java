package com.clemble.casino.goal.construction.controller;

import com.clemble.casino.goal.lifecycle.initiation.GoalInitiation;
import com.clemble.casino.goal.lifecycle.initiation.service.GoalInitiationService;
import com.clemble.casino.goal.construction.service.ServerGoalInitiationService;
import com.clemble.casino.goal.lifecycle.management.GoalRole;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com.clemble.casino.WebMapping.PRODUCES;
import static com.clemble.casino.goal.GoalWebMapping.*;

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

    @RequestMapping(method = RequestMethod.GET, value = MY_GOAL_INITIATION, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<GoalInitiation> getPending(@CookieValue("player") String player) {
        return initiationService.getPending(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GOAL_INITIATION, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GoalInitiation get(@PathVariable("goalKey") String goalKey) {
        return initiationService.get(goalKey);
    }

    @Override
    public GoalInitiation confirm(String key) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.POST, value = GOAL_INITIATION_CONFIRM, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GoalInitiation confirm(@CookieValue("player") String player, @PathVariable("goalKey") String goalKey) {
        return initiationService.confirm(player, goalKey);
    }

    @Override
    public GoalInitiation bid(String goalKey, GoalRole role) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.POST, value = GOAL_INITIATION_BID, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GoalInitiation bid(@CookieValue("player") String player, @PathVariable("goalKey") String goalKey, @RequestBody GoalRole role) {
        // Step 1. Processing player bid
        return initiationService.bid(goalKey, player, role);
    }

}
