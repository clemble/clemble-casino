package com.clemble.casino.goal.controller;

import com.clemble.casino.WebMapping;
import com.clemble.casino.event.Event;
import com.clemble.casino.goal.GoalWebMapping;
import com.clemble.casino.goal.action.GoalManagerFactoryFacade;
import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.service.GoalActionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mavarazy on 10/9/14.
 */
@RestController
public class GoalActionServiceController implements GoalActionService {

    final private GoalManagerFactoryFacade factoryFacade;

    public GoalActionServiceController(GoalManagerFactoryFacade factoryFacade) {
        this.factoryFacade = factoryFacade;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = GoalWebMapping.GOAL_ACTIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GoalEvent process(String goalKey, Event action) {
        return factoryFacade.get(goalKey).process(action);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.GOAL_STATE, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GoalState getState(String goalKey) {
        return factoryFacade.get(goalKey).getState();
    }

}
