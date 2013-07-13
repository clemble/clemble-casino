package com.gogomaya.server.game.event.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("declined")
public class InvitationDeclinedEvent implements InvitationResponseEvent {

    /**
     * Generated 02/06/13
     */
    private static final long serialVersionUID = 655381424177654890L;

    private long playerId;

    private long construction;

    @JsonCreator
    public InvitationDeclinedEvent(@JsonProperty("playerId") long playerId, @JsonProperty("construction") long construction) {
        this.playerId = playerId;
        this.construction = construction;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Override
    public long getConstruction() {
        return construction;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (playerId ^ (playerId >>> 32));
        result = prime * result + (int) (construction ^ (construction >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InvitationDeclinedEvent other = (InvitationDeclinedEvent) obj;
        if (playerId != other.playerId)
            return false;
        if (construction != other.construction)
            return false;
        return true;
    }

}
