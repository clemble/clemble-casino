package com.gogomaya.server.game.action.impl;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.action.GameEngine;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.move.GameMove;

abstract public class AbstractGameEngine<State extends GameState> implements GameEngine<State> {

    @Override
    final public State process(final State oldState, final GameMove move) {
        // Step 0. Sanity check
        if (oldState == null)
            throw GogomayaException.create(GogomayaError.GamePlayStayUndefined);
        if (move == null)
            throw GogomayaException.create(GogomayaError.GamePlayMoveUndefined);
        final long playerId = move.getPlayerId();
        // Step 1.1. Checking that move
        GameMove associatedPlayerMove = oldState.getMadeMove(playerId);
        if (associatedPlayerMove != null)
            throw GogomayaException.create(GogomayaError.GamePlayMoveAlreadyMade);
        GameMove expectedMove = oldState.getNextMove(playerId);
        if (expectedMove == null)
            throw GogomayaException.create(GogomayaError.GamePlayNoMoveExpected);
        if (expectedMove.getClass() != move.getClass())
            throw GogomayaException.create(GogomayaError.GamePlayWrongMoveType);
        // Step 2. Processing Select cell move
        State newState = safeProcess(oldState, move);
        // increase a version
        return newState;
    }

    abstract protected State safeProcess(final State oldState, final GameMove move);

}
