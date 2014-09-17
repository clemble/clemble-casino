package com.clemble.casino.server.event.goal;

import com.clemble.casino.goal.Goal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 8/18/14.
 */
public class SystemGoalCreatedEvent implements SystemGoalEvent {

    final public static String CHANNEL = "sys:goal:created";

    final private String goalKey;
    final private Goal goal;

    @JsonCreator
    public SystemGoalCreatedEvent(@JsonProperty("goalKey") String goalKey, @JsonProperty("goal") Goal goal) {
        this.goalKey = goalKey;
        this.goal = goal;
    }

    @Override
    public String getGoalKey() {
        return goalKey;
    }

    public Goal getGoal(){
        return goal;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemGoalCreatedEvent that = (SystemGoalCreatedEvent) o;

        if (goal != null ? !goal.equals(that.goal) : that.goal != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return goal != null ? goal.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "sys:" + goal.getGoalKey() +  ":" + CHANNEL;
    }
}
