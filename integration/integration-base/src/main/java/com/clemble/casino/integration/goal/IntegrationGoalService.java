package com.clemble.casino.integration.goal;

import com.clemble.casino.goal.Goal;
import com.clemble.casino.goal.service.GoalService;
import com.clemble.casino.server.goal.controller.GoalServiceController;

import java.util.Collection;

/**
 * Created by mavarazy on 8/2/14.
 */
public class IntegrationGoalService implements GoalService {

    final private String player;
    final private GoalServiceController goalService;

    public IntegrationGoalService(String player, GoalServiceController goalService) {
        this.player = player;
        this.goalService = goalService;
    }

    @Override
    public Goal getGoal(String player, String id) {
        return goalService.getGoal(player, id);
    }

    @Override
    public Collection<Goal> getGoals(String player) {
        return goalService.getGoals(player);
    }

    @Override
    public Collection<Goal> getPendingGoals(String player) {
        return goalService.getPendingGoals(player);
    }

    @Override
    public Collection<Goal> getReachedGoals(String player) {
        return goalService.getReachedGoals(player);
    }

    @Override
    public Collection<Goal> getMissedGoals(String player) {
        return goalService.getMissedGoals(player);
    }

    @Override
    public Goal addMyGoal(Goal goal) {
        return goalService.addMyGoal(player, goal);
    }

    @Override
    public Goal myGoal(String id) {
        return goalService.myGoal(player, id);
    }

    @Override
    public Collection<Goal> myGoals() {
        return goalService.getGoals(player);
    }

    @Override
    public Collection<Goal> myPendingGoals() {
        return goalService.getPendingGoals(player);
    }

    @Override
    public Collection<Goal> myReachedGoals() {
        return goalService.getReachedGoals(player);
    }

    @Override
    public Collection<Goal> myMissedGoals() {
        return goalService.getMissedGoals(player);
    }

}
