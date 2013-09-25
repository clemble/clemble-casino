package com.gogomaya.game.event.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.game.GameSessionKey;

@JsonTypeName("declined")
public class InvitationDeclinedEvent implements InvitationResponseEvent {

    /**
     * Generated 02/06/13
     */
    private static final long serialVersionUID = 655381424177654890L;

    final private long playerId;

    final private GameSessionKey session;

    @JsonCreator
    public InvitationDeclinedEvent(@JsonProperty("playerId") long playerId, @JsonProperty("session") GameSessionKey session) {
        this.playerId = playerId;
        this.session = session;
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
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (playerId ^ (playerId >>> 32));
        result = prime * result + (int) (session.hashCode());
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
        if (session != other.session)
            return false;
        return true;
    }

}
