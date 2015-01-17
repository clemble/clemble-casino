package com.clemble.casino.goal.controller;

import static com.clemble.casino.goal.GoalWebMapping.*;
import com.clemble.casino.goal.action.GoalManagerFactoryFacade;
import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.service.GoalActionService;
import com.clemble.casino.goal.repository.GoalStateRepository;
import com.clemble.casino.lifecycle.management.event.action.Action;
import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import static org.springframework.http.HttpStatus.*;
import org.springframework.web.bind.annotation.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.List;

/**
 * Created by mavarazy on 10/9/14.
 */
@RestController
public class GoalActionServiceController implements GoalActionService {

    final private GoalManagerFactoryFacade factoryFacade;
    final private GoalStateRepository stateRepository;

    public GoalActionServiceController(
        GoalManagerFactoryFacade factoryFacade,
        GoalStateRepository stateRepository) {
        this.factoryFacade = factoryFacade;
        this.stateRepository = stateRepository;
    }

    @Override
    public List<GoalState> myActive() {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = GET, value = MY_ACTIVE_GOALS, produces = PRODUCES)
    @ResponseStatus(value = OK)
    public List<GoalState> myActive(@CookieValue("player") String player) {
        return (List<GoalState>)(List<?>) stateRepository.findByPlayer(player);
    }

    @Override
    @RequestMapping(method = GET, value = PLAYER_ACTIVE_GOALS, produces = PRODUCES)
    @ResponseStatus(value = OK)
    public List<GoalState> getActive(@PathVariable("player") String player) {
        return (List<GoalState>)(List<?>) stateRepository.findByPlayer(player);
    }

    @Override
    public GoalEvent process(String goalKey, Action action) {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = POST, value = GOAL_STATE_ACTION, produces = PRODUCES)
    @ResponseStatus(value = OK)
    public GoalEvent process(@PathVariable("goalKey") String goalKey, @CookieValue("player") String player, @RequestBody Action action) {
        PlayerAction playerAction = new PlayerAction(goalKey, player, action);
        return factoryFacade.get(goalKey).process(playerAction);
    }

    @Override
    @RequestMapping(method = GET, value = GOAL_STATE, produces = PRODUCES)
    @ResponseStatus(value = OK)
    public GoalState getState(@PathVariable("goalKey") String goalKey) {
        return factoryFacade.get(goalKey).getState();
    }


}
