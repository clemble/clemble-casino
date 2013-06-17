package com.gogomaya.server.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.player.PlayerAware;

public class GogomayaFailure implements PlayerAware, SessionAware {

    /**
     * Generated 12/06/13
     */
    private static final long serialVersionUID = 8151325637613551745L;

    final private GogomayaError error;
    final private long playerId;
    final private long sessionId;

    public GogomayaFailure(final GogomayaError error) {
        this(error, PlayerAware.DEFAULT_PLAYER, SessionAware.DEFAULT_SESSION);
    }

    @JsonCreator
    public GogomayaFailure(@JsonProperty("error") final GogomayaError error, @JsonProperty("player") final long playerId,
            @JsonProperty("session") final long sessionId) {
        this.error = error;
        this.playerId = playerId;
        this.sessionId = sessionId;
    }

    public GogomayaError getError() {
        return error;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    @Override
    public long getSession() {
        return sessionId;
    }

    @Override
    public String toString() {
        return "GogomayaFailure [error = " + error + ", player = " + playerId + ", session = " + sessionId + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((error == null) ? 0 : error.hashCode());
        result = prime * result + (int) (playerId ^ (playerId >>> 32));
        result = prime * result + (int) (sessionId ^ (sessionId >>> 32));
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
        GogomayaFailure other = (GogomayaFailure) obj;
        if (error != other.error)
            return false;
        if (playerId != other.playerId)
            return false;
        if (sessionId != other.sessionId)
            return false;
        return true;
    }

}
