package com.gogomaya.server.game.event.server;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.SessionAware;

@JsonTypeName("started")
public class GameStartedEvent<State extends GameState> extends GameServerEvent<State> {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4474960027054354888L;

    private Collection<ClientEvent> nextMoves;

    public GameStartedEvent() {
    }

    public GameStartedEvent(SessionAware sessionAware) {
        super(sessionAware);
    }

    public GameStartedEvent(SessionAware sessionAware, State state) {
        super(sessionAware);
        this.setState(state);
    }

    public Collection<ClientEvent> getNextMoves() {
        return nextMoves;
    }

    public GameStartedEvent<State> setNextMoves(Collection<ClientEvent> nextMoves) {
        this.nextMoves = nextMoves;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nextMoves == null) ? 0 : nextMoves.hashCode());
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
        GameStartedEvent<State> other = (GameStartedEvent<State>) obj;
        if (nextMoves == null) {
            if (other.nextMoves != null)
                return false;
        } else if (!nextMoves.equals(other.nextMoves))
            return false;
        return true;
    }
}
