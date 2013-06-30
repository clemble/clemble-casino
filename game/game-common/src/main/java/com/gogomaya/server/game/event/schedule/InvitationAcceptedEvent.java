package com.gogomaya.server.game.event.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("accepted")
public class InvitationAcceptedEvent implements InvitationResponceEvent {

    /**
     * Generated 02/06/2013
     */
    private static final long serialVersionUID = -4465974655141746411L;

    final private long playerId;

    final private long construction;

    @JsonCreator
    public InvitationAcceptedEvent(@JsonProperty("construction") long construction, @JsonProperty("playerId") long playerId) {
        this.construction = construction;
        this.playerId = playerId;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    @Override
    public long getConstruction() {
        return construction;
    }

    @Override
    public int hashCode() {
        return 31 * (31 + (int) (playerId ^ (playerId >>> 32))) + (int) (construction ^ (construction >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InvitationAcceptedEvent other = (InvitationAcceptedEvent) obj;
        if (playerId != other.playerId)
            return false;
        if (construction != other.construction)
            return false;
        return true;
    }

}
