package com.clemble.casino.server.event.goal;

import com.clemble.casino.goal.GoalJudgeInvitation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 8/23/14.
 */
public class SystemGoalJudgeInvitationAcceptedEvent implements SystemGoalEvent {

    final public static String CHANNEL = "goal:judge:invitation:accepted";

    final private GoalJudgeInvitation invitation;

    @JsonCreator
    public SystemGoalJudgeInvitationAcceptedEvent(@JsonProperty("invitation") GoalJudgeInvitation invitation) {
        this.invitation = invitation;
    }

    public GoalJudgeInvitation getInvitation() {
        return invitation;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public String toString() {
        return "sys:" + invitation.getJudge() +  ":" + CHANNEL;
    }

}
