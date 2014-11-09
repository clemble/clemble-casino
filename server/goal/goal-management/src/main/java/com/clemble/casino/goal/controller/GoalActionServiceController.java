package com.clemble.casino.goal.controller;

import com.clemble.casino.WebMapping;
import com.clemble.casino.event.Event;
import com.clemble.casino.goal.GoalWebMapping;
import com.clemble.casino.goal.action.GoalManagerFactoryFacade;
import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.service.GoalActionService;
import com.clemble.casino.goal.repository.GoalStateRepository;
import com.clemble.casino.lifecycle.management.event.action.Action;
import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import com.clemble.casino.player.service.PlayerConnectionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Created by mavarazy on 10/9/14.
 */
@RestController
public class GoalActionServiceController implements GoalActionService {

    final private GoalManagerFactoryFacade factoryFacade;
    final private GoalStateRepository stateRepository;
    final private PlayerConnectionService connectionService;

    public GoalActionServiceController(
        GoalManagerFactoryFacade factoryFacade,
        GoalStateRepository stateRepository,
        PlayerConnectionService connectionService) {
        this.factoryFacade = factoryFacade;
        this.stateRepository = stateRepository;
        this.connectionService = connectionService;
    }

    @Override
    public List<GoalState> myActive() {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.MY_ACTIVE_GOALS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<GoalState> myActive(@CookieValue("player") String player) {
        return stateRepository.findByPlayer(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.PLAYER_ACTIVE_GOALS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<GoalState> getActive(@PathVariable("player") String player) {
        return stateRepository.findByPlayer(player);
    }

    @Override
    public List<GoalState> myConnectionsTimeLine() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.MY_CONNECTIONS_TIMELINE, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<GoalState> myConnectionsTimeLine(@CookieValue("player") String player) {
        return getConnectionsTimeLine(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.PLAYER_CONNECTIONS_TIMELINE, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<GoalState> getConnectionsTimeLine(@PathVariable("player") String player) {
        // Step 1. Fetching connections
        Set<String> connections = connectionService.getConnectedConnection(player);
        // Step 2. Querying for active connections state
        return stateRepository.findByPlayerIn(connections);
    }

    @Override
    public GoalEvent process(String goalKey, Action action) {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = RequestMethod.POST, value = GoalWebMapping.GOAL_ACTIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GoalEvent process(@PathVariable("goalKey") String goalKey, @CookieValue("player") String player, @RequestBody Action action) {
        PlayerAction playerAction = new PlayerAction(goalKey, player, action);
        return factoryFacade.get(goalKey).process(playerAction);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.GOAL_STATE, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GoalState getState(@PathVariable("goalKey") String goalKey) {
        return factoryFacade.get(goalKey).getState();
    }

}
