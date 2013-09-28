package com.gogomaya.game.event.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.event.ConstructionEvent;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.player.PlayerAware;

@JsonTypeName("canceled")
public class GameCanceledEvent implements ConstructionEvent, PlayerAware {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = 1L;

    final private String player;
    final private GameSessionKey session;

    @JsonCreator
    public GameCanceledEvent(@JsonProperty("session") GameSessionKey session, @JsonProperty(PlayerAware.JSON_ID) String player) {
        this.session = session;
        this.player = player;
    }

    @Override
    public GameSessionKey getSession() {
        return session;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public int hashCode() {
        return session.hashCode();
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

}
