package com.clemble.casino.server.event.goal;

import com.clemble.casino.goal.GoalJudgeInvitation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 8/18/14.
 * TODO add notification mechanism for GoalJudgeInvitation
 */
public class SystemGoalJudgeInvitationCreatedEvent implements SystemGoalEvent {

    final public static String CHANNEL = "goal:invitation:created";

    final private GoalJudgeInvitation invitation;

    @JsonCreator
    public SystemGoalJudgeInvitationCreatedEvent(@JsonProperty("invitation") GoalJudgeInvitation invitation) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemGoalJudgeInvitationCreatedEvent that = (SystemGoalJudgeInvitationCreatedEvent) o;

        if (invitation != null ? !invitation.equals(that.invitation) : that.invitation != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return invitation != null ? invitation.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "sys:" + invitation.getGoalKey() +  ":" + CHANNEL + ":" + invitation.getJudge();
    }

}
