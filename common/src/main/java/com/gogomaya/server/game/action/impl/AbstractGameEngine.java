package com.gogomaya.server.game.action.impl;

import com.gogomaya.server.game.action.GameEngine;
import com.gogomaya.server.game.action.GamePlayerState;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.move.GameMove;

abstract public class AbstractGameEngine<S extends GameState<M, PS>, M extends GameMove, PS extends GamePlayerState> implements GameEngine<S, M> {

    @Override
    final public S process(final S oldState, final M move) {
        // Step 0. Sanity check
        if (oldState == null)
            throw new IllegalArgumentException("old state can't be null");
        if (move == null)
            return oldState;
        final long playerId = move.getPlayerId();
        // Step 1.1. Checking that move
        M associatedPlayerMove = oldState.getMadeMove(playerId);
        if (associatedPlayerMove != null)
            return oldState;
        M expectedMove = oldState.getNextMove(playerId);
        if (expectedMove == null)
            return oldState;
        if (expectedMove.getClass() != move.getClass())
            return oldState;
        // Step 2. Processing Select cell move
        return safeProcess(oldState, move);
    }

    abstract protected S safeProcess(final S oldState, final M move);

}
