package com.gogomaya.game.event.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.game.GameSessionKey;

@JsonTypeName("accepted")
public class InvitationAcceptedEvent implements InvitationResponseEvent {

    /**
     * Generated 02/06/2013
     */
    private static final long serialVersionUID = -4465974655141746411L;

    final private long playerId;
    final private GameSessionKey session;

    @JsonCreator
    public InvitationAcceptedEvent(@JsonProperty("session") GameSessionKey session, @JsonProperty("playerId") long playerId) {
        this.session = session;
        this.playerId = playerId;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    @Override
    public GameSessionKey getSession() {
        return session;
    }

    @Override
    public int hashCode() {
        return 31 * (31 + (int) (playerId ^ (playerId >>> 32))) + (int) (session.hashCode());
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
        return session.equals(other.session);
    }

}
