package com.clemble.casino.server.event.goal;

import com.clemble.casino.goal.GoalJudgeDuty;
import com.clemble.casino.server.event.SystemEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 8/23/14.
 */
public class SystemGoalJudgeDutyDueEvent implements SystemGoalEvent {

    final public static String CHANNEL = "goal:judge:duty:due";

    final private GoalJudgeDuty duty;

    @JsonCreator
    public SystemGoalJudgeDutyDueEvent(@JsonProperty("duty") GoalJudgeDuty duty) {
        this.duty = duty;
    }

    public GoalJudgeDuty getDuty() {
        return duty;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
