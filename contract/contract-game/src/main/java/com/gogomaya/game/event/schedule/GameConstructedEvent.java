package com.gogomaya.game.event.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.event.GameConstructionEvent;

@JsonTypeName("gameConstructed")
public class GameConstructedEvent implements GameConstructionEvent {

    /**
     * Generated 24/06/13
     */
    private static final long serialVersionUID = 1069615920429317027L;

    final private long session;

    @JsonCreator
    public GameConstructedEvent(@JsonProperty("session") long session) {
        this.session = session;
    }

    @Override
    public long getSession() {
        return session;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        GameConstructedEvent other = (GameConstructedEvent) obj;
        if (session != other.session)
            return false;
        return true;
    }

}
