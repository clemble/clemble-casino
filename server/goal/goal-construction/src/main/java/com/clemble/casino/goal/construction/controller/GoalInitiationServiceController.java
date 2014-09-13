package com.clemble.casino.goal.construction.controller;

import com.clemble.casino.goal.construction.GoalInitiation;
import com.clemble.casino.goal.construction.service.GoalInitiationService;
import com.clemble.casino.goal.construction.service.ServerGoalInitiationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.clemble.casino.goal.GoalWebMapping.GOAL_INITIATION_PENDING;
import static com.clemble.casino.web.mapping.WebMapping.PRODUCES;

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
    public Collection<GoalInitiation> getPending(String player) {
        return initiationService.getPending(player);
    }

}
