package com.clemble.casino.integration.goal;

import com.clemble.casino.goal.controller.FriendGoalServiceController;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.service.FriendGoalService;

import java.util.List;

/**
 * Created by mavarazy on 11/18/14.
 */
public class IntegrationFriendGoalService implements FriendGoalService {

    final private String player;
    final private FriendGoalServiceController timelineService;

    public IntegrationFriendGoalService(String player, FriendGoalServiceController timelineService) {
        this.player = player;
        this.timelineService = timelineService;
    }

    @Override
    public GoalState myFriendGoal(String goalKey) {
        return timelineService.myFriendGoal(player, goalKey);
    }

    @Override
    public List<GoalState> myFriendGoals() {
        return timelineService.myFriendGoals(player);
    }

    @Override
    public GoalState getFriendGoal(String player, String goalKey) {
        return timelineService.getFriendGoal(player, goalKey);
    }

    @Override
    public List<GoalState> getFriendGoals(String player) {
        return timelineService.getFriendGoals(player);
    }
}
