package com.clemble.casino.goal.action;

import com.clemble.casino.event.Event;
import com.clemble.casino.goal.GoalPartsAware;
import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.event.action.GoalStatusUpdateAction;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalContext;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalReachedEvent;
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
    private int parts;
    private GoalContext context;
    private int progress;

    @JsonCreator
    public SelfGoalState(
        @JsonProperty("goalKey") String goalKey,
        @JsonProperty("configuration") GoalConfiguration configuration,
        @JsonProperty("context") GoalContext context,
        @JsonProperty("status") String status,
        @JsonProperty("progress") int progress,
        @JsonProperty("parts") int parts) {
        this.goalKey = goalKey;
        this.configuration = configuration;
        this.context = context;
        this.status = status;
        this.progress = progress;
        this.parts = parts;
    }

    @Override
    public String getGoalKey() {
        return goalKey;
    }

    public int getParts() {
        return parts;
    }

    @Override
    public GoalContext getContext() {
        return context;
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
            if(this.progress >= parts) {
                return new GoalReachedEvent(goalKey);
            } else {
                return new GoalStatusUpdatedEvent(goalKey, statusUpdateAction.getPlayer(), status, progress);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

}
