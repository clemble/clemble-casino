package com.gogomaya.server.game.event.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.event.ScheduleEvent;
import com.gogomaya.server.player.PlayerAware;

@JsonTypeName("added")
public class PlayerAddedEvent implements ScheduleEvent, PlayerAware {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = -5442563161615470910L;

    final private long session;
    final private long player;

    @JsonCreator
    public PlayerAddedEvent(@JsonProperty("session") long session, @JsonProperty("playerId") long player) {
        this.session = session;
        this.player = player;
    }

    @Override
    public long getSession() {
        return session;
    }

    @Override
    public long getPlayerId() {
        return player;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        PlayerAddedEvent other = (PlayerAddedEvent) obj;
        if (player != other.player)
            return false;
        if (session != other.session)
            return false;
        return true;
    }

}
