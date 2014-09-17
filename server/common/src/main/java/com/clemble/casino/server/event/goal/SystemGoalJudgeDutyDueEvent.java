package com.clemble.casino.server.event.goal;

import com.clemble.casino.goal.GoalJudgeDuty;
import com.clemble.casino.server.event.SystemEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 8/23/14.
 */
public class SystemGoalJudgeDutyDueEvent implements SystemGoalEvent {

    final public static String CHANNEL = "sys:goal:judge:duty:due";

    final private String goalKey;
    final private GoalJudgeDuty duty;

    @JsonCreator
    public SystemGoalJudgeDutyDueEvent(@JsonProperty("goalKey") String goalKey, @JsonProperty("duty") GoalJudgeDuty duty) {
        this.goalKey = goalKey;
        this.duty = duty;
    }

    @Override
    public String getGoalKey() {
        return goalKey;
    }

    public GoalJudgeDuty getDuty() {
        return duty;
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

        SystemGoalJudgeDutyDueEvent that = (SystemGoalJudgeDutyDueEvent) o;

        if (!duty.equals(that.duty)) return false;
        if (!goalKey.equals(that.goalKey)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = goalKey.hashCode();
        result = 31 * result + duty.hashCode();
        return result;
    }

}
