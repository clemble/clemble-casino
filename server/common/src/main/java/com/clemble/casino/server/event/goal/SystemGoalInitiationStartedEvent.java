package com.clemble.casino.server.event.goal;

import com.clemble.casino.goal.lifecycle.initiation.GoalInitiation;
import com.clemble.casino.goal.lifecycle.initiation.GoalInitiationAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 1/3/15.
 */
public class SystemGoalInitiationStartedEvent implements SystemGoalEvent, GoalInitiationAware {

    final public static String CHANNEL = "sys:goal:initiation:start";

    final private String goalKey;
    final private GoalInitiation initiation;

    @JsonCreator
    public SystemGoalInitiationStartedEvent(@JsonProperty("goalKey") String goalKey, @JsonProperty("initiation") GoalInitiation initiation) {
        this.goalKey = goalKey;
        this.initiation = initiation;
    }

    @Override
    public String getGoalKey() {
        return goalKey;
    }

    @Override
    public GoalInitiation getInitiation() {
        return initiation;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemGoalInitiationStartedEvent that = (SystemGoalInitiationStartedEvent) o;

        if (!goalKey.equals(that.goalKey)) return false;
        if (!initiation.equals(that.initiation)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = goalKey.hashCode();
        result = 31 * result + initiation.hashCode();
        return result;
    }

}
