package com.gogomaya.server.game.action.impl;

import com.gogomaya.server.game.action.GameEngine;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.move.GameMove;

abstract public class AbstractGameEngine<State extends GameState> implements GameEngine<State> {

    @Override
    final public State process(final State oldState, final GameMove move) {
        // Step 0. Sanity check
        if (oldState == null)
            throw new IllegalArgumentException("old state can't be null");
        if (move == null)
            throw new IllegalArgumentException("move can't be null");
        final long playerId = move.getPlayerId();
        // Step 1.1. Checking that move
        GameMove associatedPlayerMove = oldState.getMadeMove(playerId);
        if (associatedPlayerMove != null)
            throw new IllegalArgumentException("Associated player already made a move " + oldState.getMadeMove(playerId));
        GameMove expectedMove = oldState.getNextMove(playerId);
        if (expectedMove == null)
            throw new IllegalArgumentException("No move expected from the player " + move);
        if (expectedMove.getClass() != move.getClass())
            throw new IllegalArgumentException("Move of the wrong class " + move.getClass() + " expected " + expectedMove.getClass());
        // Step 2. Processing Select cell move
        State newState = safeProcess(oldState, move);
        // increase a version
        newState.incrementVersion();
        return newState;
    }

    abstract protected State safeProcess(final State oldState, final GameMove move);

}
