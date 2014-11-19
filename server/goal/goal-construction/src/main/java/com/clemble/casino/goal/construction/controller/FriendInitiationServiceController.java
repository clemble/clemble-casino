package com.clemble.casino.goal.construction.controller;

import com.clemble.casino.WebMapping;
import com.clemble.casino.goal.GoalWebMapping;
import com.clemble.casino.goal.construction.repository.GoalInitiationRepository;
import com.clemble.casino.goal.lifecycle.initiation.GoalInitiation;
import com.clemble.casino.goal.lifecycle.initiation.service.FriendInitiationService;
import com.clemble.casino.lifecycle.initiation.InitiationState;
import com.clemble.casino.player.service.PlayerConnectionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Created by mavarazy on 11/19/14.
 */
@RestController
public class FriendInitiationServiceController implements FriendInitiationService {

    final private GoalInitiationRepository initiationRepository;
    final private PlayerConnectionService connectionService;

    public FriendInitiationServiceController(
        GoalInitiationRepository initiationRepository,
        PlayerConnectionService connectionService) {
        this.initiationRepository = initiationRepository;
        this.connectionService = connectionService;
    }

    @Override
    public GoalInitiation myFriendInitiation(String goalKey) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.MY_FRIEND_INITIATION, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GoalInitiation myFriendInitiation(@CookieValue("player") String player, @PathVariable("goalKey") String goalKey) {
        return initiationRepository.findOne(goalKey);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.PLAYER_FRIEND_INITIATION, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GoalInitiation getFriendInitiation(@PathVariable("player") String player, @PathVariable("goalKey") String goalKey) {
        return initiationRepository.findOne(goalKey);
    }

    @Override
    public List<GoalInitiation> myFriendInitiations() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.MY_FRIEND_INITIATIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<GoalInitiation> myFriendInitiations(@CookieValue("player") String player) {
        return getFriendInitiations(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.PLAYER_FRIEND_INITIATIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<GoalInitiation> getFriendInitiations(@PathVariable("player") String player) {
        // Step 1. Fetching connections
        Set<String> connections = connectionService.getConnections(player);
        // Step 2. Querying for active connections state
        return initiationRepository.findByPlayerInAndState(connections, InitiationState.pending);
    }

}
