package com.gogomaya.server.game.event.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.event.GameConstructionEvent;
import com.gogomaya.server.player.PlayerAware;

@JsonTypeName("canceled")
public class GameCanceledEvent implements GameConstructionEvent, PlayerAware {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = 1L;

    final private long session;

    final private long playerId;

    @JsonCreator
    public GameCanceledEvent(@JsonProperty("session") long session, @JsonProperty("playerId") long playerId) {
        this.session = session;
        this.playerId = playerId;
    }

    @Override
    public long getSession() {
        return session;
    }

    @Override
    public int hashCode() {
        return 31 + (int) (session ^ (session >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return session == ((GameCanceledEvent) obj).session;
    }

    public long getPlayerId() {
        return playerId;
    }

}
