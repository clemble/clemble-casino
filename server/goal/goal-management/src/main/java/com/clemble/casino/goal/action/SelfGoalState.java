package com.clemble.casino.goal.action;

import com.clemble.casino.event.Event;
import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.event.action.GoalStatusUpdateAction;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalStatusUpdatedEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by mavarazy on 10/9/14.
 */
@JsonTypeName("goal:self")
public class SelfGoalState implements GoalState {

    private String goalKey;
    private GoalConfiguration configuration;
    private String status;
    private int progress;

    @JsonCreator
    public SelfGoalState(
        @JsonProperty("goalKey") String goalKey,
        @JsonProperty("configuration") GoalConfiguration configuration,
        @JsonProperty("status") String status,
        @JsonProperty("property") int progress) {
        this.goalKey = goalKey;
        this.configuration = configuration;
        this.status = status;
        this.progress = progress;
    }

    @Override
    public String getGoalKey() {
        return goalKey;
    }

    @Override
    public GoalConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public GoalEvent process(Event action){
        if(action instanceof GoalStatusUpdateAction) {
            GoalStatusUpdateAction statusUpdateAction = ((GoalStatusUpdateAction) action);
            this.status = statusUpdateAction.getStatus();
            this.progress = progress + statusUpdateAction.getProgress();
            return new GoalStatusUpdatedEvent(statusUpdateAction.getGoalKey(), statusUpdateAction.getPlayer(), status, progress);
        } else {
            throw new IllegalArgumentException();
        }
    }

}
