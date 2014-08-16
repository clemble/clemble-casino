package com.clemble.casino.integration.goal;

import com.clemble.casino.goal.Goal;
import com.clemble.casino.goal.GoalStatus;
import com.clemble.casino.goal.service.GoalService;
import com.clemble.casino.server.goal.controller.GoalServiceController;

import java.util.Collection;

/**
 * Created by mavarazy on 8/2/14.
 */
public class IntegrationGoalService implements GoalService {

    final private String player;
    final private GoalServiceController goalController;

    public IntegrationGoalService(String player, GoalServiceController goalController) {
        this.player = player;
        this.goalController = goalController;
    }

    @Override
    public GoalStatus myGoalStatuses(String id) {
        return goalController.myGoalStatuses(player, id);
    }

    @Override
    public Goal getGoal(String player, String id) {
        return goalController.getGoal(player, id);
    }

    @Override
    public Collection<Goal> getGoals(String player) {
        return goalController.getGoals(player);
    }

    @Override
    public Collection<Goal> getPendingGoals(String player) {
        return goalController.getPendingGoals(player);
    }

    @Override
    public Collection<Goal> getReachedGoals(String player) {
        return goalController.getReachedGoals(player);
    }

    @Override
    public Collection<Goal> getMissedGoals(String player) {
        return goalController.getMissedGoals(player);
    }

    @Override
    public Goal addMyGoal(Goal goal) {
        return goalController.addMyGoal(player, goal);
    }

    @Override
    public GoalStatus updateMyGoal(String id, GoalStatus status) {
        return goalController.updateMyGoal(player, id, status);
    }

    @Override
    public Goal myGoal(String id) {
        return goalController.myGoal(player, id);
    }

    @Override
    public Collection<Goal> myGoals() {
        return goalController.getGoals(player);
    }

    @Override
    public Collection<Goal> myPendingGoals() {
        return goalController.getPendingGoals(player);
    }

    @Override
    public Collection<Goal> myReachedGoals() {
        return goalController.getReachedGoals(player);
    }

    @Override
    public Collection<Goal> myMissedGoals() {
        return goalController.getMissedGoals(player);
    }

}
