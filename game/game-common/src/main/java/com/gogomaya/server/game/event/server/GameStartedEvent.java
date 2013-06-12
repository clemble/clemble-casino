package com.gogomaya.server.game.event.server;

import java.util.Collection;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.SessionAware;

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

    public Collection<ClientEvent> getNextMoves() {
        return nextMoves;
    }

    public GameStartedEvent<State> setNextMoves(Collection<ClientEvent> nextMoves) {
        this.nextMoves = nextMoves;
        return this;
    }
}
