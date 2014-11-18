package com.clemble.casino.goal.controller;

import com.clemble.casino.WebMapping;
import com.clemble.casino.goal.GoalWebMapping;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.service.GoalTimelineService;
import com.clemble.casino.goal.repository.GoalStateRepository;
import com.clemble.casino.player.service.PlayerConnectionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Created by mavarazy on 11/18/14.
 */
@RestController
public class GoalTimelineServiceController implements GoalTimelineService {

    final private GoalStateRepository stateRepository;
    final private PlayerConnectionService connectionService;

    public GoalTimelineServiceController(
        GoalStateRepository stateRepository,
        PlayerConnectionService connectionService) {
        this.stateRepository = stateRepository;
        this.connectionService = connectionService;
    }

    @Override
    public GoalState myConnectionTimeLine(String goalKey) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.MY_CONNECTIONS_TIMELINES_GOAL, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GoalState myConnectionTimeLine(@CookieValue("player") String player, @PathVariable("goalKey") String goalKey) {
        return stateRepository.findOne(goalKey);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.PLAYER_CONNECTIONS_TIMELINES_GOAL, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GoalState getConnectionTimeLine(@PathVariable("player") String player, @PathVariable("goalKey") String goalKey) {
        return stateRepository.findOne(goalKey);
    }


    @Override
    public List<GoalState> myConnectionsTimeLine() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.MY_CONNECTIONS_TIMELINES, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<GoalState> myConnectionsTimeLine(@CookieValue("player") String player) {
        return getConnectionsTimeLine(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.PLAYER_CONNECTIONS_TIMELINES, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<GoalState> getConnectionsTimeLine(@PathVariable("player") String player) {
        // Step 1. Fetching connections
        Set<String> connections = connectionService.getConnections(player);
        // Step 2. Querying for active connections state
        return stateRepository.findByPlayerIn(connections);
    }

}
