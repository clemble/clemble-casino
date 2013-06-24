package com.gogomaya.server.game.event.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.event.ScheduleEvent;
import com.gogomaya.server.game.GameState;

@JsonTypeName("canceled")
public class GameCanceled<State extends GameState> implements ScheduleEvent {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = 1L;

    final private long session;

    @JsonCreator
    public GameCanceled(@JsonProperty("session") long session) {
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
        GameCanceled other = (GameCanceled) obj;
        if (session != other.session)
            return false;
        return true;
    }

}
