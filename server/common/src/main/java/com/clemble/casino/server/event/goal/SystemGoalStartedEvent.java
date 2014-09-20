package com.clemble.casino.server.event.goal;

import com.clemble.casino.goal.construction.GoalInitiation;
import com.clemble.casino.goal.construction.GoalInitiationAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 9/20/14.
 */
public class SystemGoalStartedEvent implements SystemGoalEvent, GoalInitiationAware {

    final public static String CHANNEL = "sys:goal:started";

    final private String goalKey;
    final private GoalInitiation initiation;

    @JsonCreator
    public SystemGoalStartedEvent(@JsonProperty("goalKey") String goalKey, @JsonProperty("initiation") GoalInitiation initiation) {
        this.goalKey = goalKey;
        this.initiation = initiation;
    }

    @Override
    public GoalInitiation getInitiation() {
        return initiation;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemGoalStartedEvent that = (SystemGoalStartedEvent) o;

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
