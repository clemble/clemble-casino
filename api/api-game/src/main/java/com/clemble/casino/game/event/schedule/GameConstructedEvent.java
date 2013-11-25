package com.clemble.casino.game.event.schedule;

import com.clemble.casino.event.ConstructionEvent;
import com.clemble.casino.game.GameSessionKey;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("gameConstructed")
public class GameConstructedEvent implements ConstructionEvent {

    /**
     * Generated 24/06/13
     */
    private static final long serialVersionUID = 1069615920429317027L;

    final private GameSessionKey session;

    @JsonCreator
    public GameConstructedEvent(@JsonProperty("session") GameSessionKey session) {
        this.session = session;
    }

    @Override
    public GameSessionKey getSession() {
        return session;
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
        GameConstructedEvent other = (GameConstructedEvent) obj;
        return session.equals(other.session);
    }

    @Override
    public String toString() {
        return "constructed:" + session;
    }

}
