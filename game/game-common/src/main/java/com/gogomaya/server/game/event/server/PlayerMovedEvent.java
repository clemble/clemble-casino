package com.gogomaya.server.game.event.server;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.player.PlayerAware;

public class PlayerMovedEvent<State extends GameState> extends GameServerEvent<State> implements PlayerAware {

    /**
     * Generated 07/05/2013
     */
    private static final long serialVersionUID = -3272848407005579296L;

    private ClientEvent madeMove;

    private Collection<ClientEvent> nextMoves;

    public PlayerMovedEvent() {
    }

    public PlayerMovedEvent(SessionAware sessionAware) {
        super(sessionAware);
    }

    public ClientEvent getMadeMove() {
        return madeMove;
    }

    public PlayerMovedEvent<State> setMadeMove(ClientEvent madeMove) {
        this.madeMove = madeMove;
        return this;
    }

    public Collection<ClientEvent> getNextMoves() {
        return nextMoves;
    }

    public PlayerMovedEvent<State> setNextMoves(Collection<ClientEvent> nextMoves) {
        this.nextMoves = nextMoves;
        return this;
    }

    @Override
    @JsonIgnore
    public long getPlayerId() {
        return madeMove.getPlayerId();
    }

}
