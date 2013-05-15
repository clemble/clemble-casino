package com.gogomaya.server.game.event;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gogomaya.server.event.PlayerAwareEvent;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.move.GameMove;

public class PlayerMovedEvent<State extends GameState> extends GameEvent<State> implements PlayerAwareEvent {

    /**
     * Generated 07/05/2013
     */
    private static final long serialVersionUID = -3272848407005579296L;

    private GameMove madeMove;

    private Collection<GameMove> nextMoves;

    public GameMove getMadeMove() {
        return madeMove;
    }

    public PlayerMovedEvent<State> setMadeMove(GameMove madeMove) {
        this.madeMove = madeMove;
        return this;
    }

    public Collection<GameMove> getNextMoves() {
        return nextMoves;
    }

    public PlayerMovedEvent<State> setNextMoves(Collection<GameMove> nextMoves) {
        this.nextMoves = nextMoves;
        return this;
    }

    @Override
    @JsonIgnore
    public long getPlayerId() {
        return madeMove.getPlayerId();
    }

}
