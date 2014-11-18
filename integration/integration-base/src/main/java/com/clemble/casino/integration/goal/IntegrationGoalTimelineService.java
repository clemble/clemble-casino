package com.clemble.casino.integration.goal;

import com.clemble.casino.goal.controller.GoalTimelineServiceController;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.service.GoalTimelineService;

import java.util.List;

/**
 * Created by mavarazy on 11/18/14.
 */
public class IntegrationGoalTimelineService implements GoalTimelineService {

    final private String player;
    final private GoalTimelineServiceController timelineService;

    public IntegrationGoalTimelineService(String player, GoalTimelineServiceController timelineService) {
        this.player = player;
        this.timelineService = timelineService;
    }

    @Override
    public GoalState myConnectionTimeLine(String goalKey) {
        return timelineService.myConnectionTimeLine(player, goalKey);
    }

    @Override
    public List<GoalState> myConnectionsTimeLine() {
        return timelineService.myConnectionsTimeLine(player);
    }

    @Override
    public GoalState getConnectionTimeLine(String player, String goalKey) {
        return timelineService.getConnectionTimeLine(player, goalKey);
    }

    @Override
    public List<GoalState> getConnectionsTimeLine(String player) {
        return timelineService.getConnectionsTimeLine(player);
    }
}
