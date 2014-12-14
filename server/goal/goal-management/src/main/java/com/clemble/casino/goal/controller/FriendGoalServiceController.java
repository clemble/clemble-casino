package com.clemble.casino.goal.controller;

import com.clemble.casino.WebMapping;
import com.clemble.casino.goal.GoalWebMapping;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.service.FriendGoalService;
import com.clemble.casino.goal.repository.ShortGoalStateRepository;
import com.clemble.casino.player.service.PlayerConnectionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Created by mavarazy on 11/18/14.
 */
@RestController
public class FriendGoalServiceController implements FriendGoalService {

    final private ShortGoalStateRepository stateRepository;
    final private PlayerConnectionService connectionService;

    public FriendGoalServiceController(
        ShortGoalStateRepository stateRepository,
        PlayerConnectionService connectionService) {
        this.stateRepository = stateRepository;
        this.connectionService = connectionService;
    }

    @Override
    public GoalState myFriendGoal(String goalKey) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.MY_FRIEND_GOAL, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GoalState myFriendGoal(@CookieValue("player") String player, @PathVariable("goalKey") String goalKey) {
        return stateRepository.findOne(goalKey);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.PLAYER_FRIEND_GOAL, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GoalState getFriendGoal(@PathVariable("player") String player, @PathVariable("goalKey") String goalKey) {
        return stateRepository.findOne(goalKey);
    }

    @Override
    public List<GoalState> myFriendGoals() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.MY_FRIEND_GOALS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<GoalState> myFriendGoals(@CookieValue("player") String player) {
        return getFriendGoals(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.PLAYER_FRIEND_GOALS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<GoalState> getFriendGoals(@PathVariable("player") String player) {
        // Step 1. Fetching connections
        Set<String> connections = connectionService.getConnections(player);
        // Step 2. Querying for active connections state
        return (List<GoalState>)(List<?>) stateRepository.findByPlayerIn(connections);
    }

}
