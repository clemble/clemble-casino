package com.gogomaya.server.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gogomaya.server.error.GogomayaErrorFormat.GogomayaFailureDeserializer;
import com.gogomaya.server.error.GogomayaErrorFormat.GogomayaFailureSerializer;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.player.PlayerAware;

@JsonSerialize(using = GogomayaFailureSerializer.class)
@JsonDeserialize(using = GogomayaFailureDeserializer.class)
public class GogomayaFailure implements PlayerAware, SessionAware {

    /**
     * Generated 12/06/13
     */
    private static final long serialVersionUID = 8151325637613551745L;

    final private GogomayaError error;
    final private long player;
    final private long session;

    public GogomayaFailure(final GogomayaError error) {
        this(error, PlayerAware.DEFAULT_PLAYER, SessionAware.DEFAULT_SESSION);
    }

    public GogomayaFailure(final GogomayaError error, final long playerId) {
        this(error, playerId, SessionAware.DEFAULT_SESSION);
    }

    @JsonCreator
    public GogomayaFailure(@JsonProperty("error") final GogomayaError error, @JsonProperty("playerId") final long playerId,
            @JsonProperty("session") final long sessionId) {
        this.error = error;
        this.player = playerId;
        this.session = sessionId;
    }

    public GogomayaError getError() {
        return error;
    }

    @Override
    public long getPlayerId() {
        return player;
    }

    @Override
    public long getSession() {
        return session;
    }

    @Override
    public String toString() {
        return "GogomayaFailure [error = " + error + ", player = " + player + ", session = " + session + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((error == null) ? 0 : error.hashCode());
        result = prime * result + (int) (player ^ (player >>> 32));
        result = prime * result + (int) (session ^ (session >>> 32));
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
        if (player != other.player)
            return false;
        if (session != other.session)
            return false;
        return true;
    }

}
