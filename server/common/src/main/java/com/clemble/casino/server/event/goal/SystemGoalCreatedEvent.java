package com.clemble.casino.server.event.goal;

import com.clemble.casino.goal.Goal;
import com.clemble.casino.goal.GoalKey;
import com.clemble.casino.server.event.SystemEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 8/18/14.
 */
public class SystemGoalCreatedEvent implements SystemGoalEvent {

    final public static String CHANNEL = "goal:created";

    final private Goal goal;

    @JsonCreator
    public SystemGoalCreatedEvent(@JsonProperty("goal") Goal goal) {
        this.goal = goal;
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
        return "sys:" + goal.getPlayer() +  ":" + CHANNEL;
    }
}