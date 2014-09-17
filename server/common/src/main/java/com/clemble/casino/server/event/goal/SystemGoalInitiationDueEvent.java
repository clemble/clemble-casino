package com.clemble.casino.server.event.goal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 9/13/14.
 */

public class SystemGoalInitiationDueEvent implements SystemGoalEvent {

    final public static String CHANNEL = "sys:goal:initiation:due";

    final private String goalKey;

    @JsonCreator
    public SystemGoalInitiationDueEvent(@JsonProperty("goalKey") String goalKey) {
        this.goalKey = goalKey;
    }

    @Override
    public String getGoalKey() {
        return goalKey;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public String toString() {
        return "sys:" + goalKey +  ":" + CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemGoalInitiationDueEvent that = (SystemGoalInitiationDueEvent) o;

        if (!goalKey.equals(that.goalKey)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return goalKey.hashCode();
    }

}
