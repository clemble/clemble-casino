package com.clemble.casino.integration.goal;

import com.clemble.casino.goal.construction.controller.FriendInitiationServiceController;
import com.clemble.casino.goal.lifecycle.initiation.GoalInitiation;
import com.clemble.casino.goal.lifecycle.initiation.service.FriendInitiationService;

import java.util.List;

/**
 * Created by mavarazy on 11/19/14.
 */
public class IntegrationFriendInitiationService implements FriendInitiationService {

    final private String player;
    final private FriendInitiationServiceController friendInitiationService;

    public IntegrationFriendInitiationService(
        String player,
        FriendInitiationServiceController friendInitiationService
    ) {
        this.player = player;
        this.friendInitiationService = friendInitiationService;
    }

    @Override
    public GoalInitiation myFriendInitiation(String goalKey) {
        return friendInitiationService.myFriendInitiation(player, goalKey);
    }

    @Override
    public List<GoalInitiation> myFriendInitiations() {
        return friendInitiationService.myFriendInitiations(player);
    }

    @Override
    public List<GoalInitiation> getFriendInitiations(String player) {
        return friendInitiationService.getFriendInitiations(player);
    }

    @Override
    public GoalInitiation getFriendInitiation(String player, String goalKey) {
        return friendInitiationService.getFriendInitiation(player, goalKey);
    }

}
