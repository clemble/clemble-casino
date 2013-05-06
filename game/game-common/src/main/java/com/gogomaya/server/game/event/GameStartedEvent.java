package com.gogomaya.server.game.event;

import java.util.Collection;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.move.GameMove;

public class GameStartedEvent<State extends GameState> extends GameEvent<State> {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4474960027054354888L;

    private Collection<GameMove> nextMoves;

    public Collection<GameMove> getNextMoves() {
        return nextMoves;
    }

    public void setNextMoves(Collection<GameMove> nextMoves) {
        this.nextMoves = nextMoves;
    }
}
