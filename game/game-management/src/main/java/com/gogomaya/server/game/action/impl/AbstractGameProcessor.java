package com.gogomaya.server.game.action.impl;

import java.util.Collection;

import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.event.GameEvent;

abstract public class AbstractGameProcessor<State extends GameState> implements GameProcessor<State> {

    final private GameProcessor<State> delegate;

    public AbstractGameProcessor(final GameProcessor<State> delegate) {
        this.delegate = delegate;
    }

    @Override
    final public Collection<GameEvent<State>> process(final State state, final GameMove move) {
        // Step 1. Invoking process movement
        processMovement(state, move);
        // Step 2. Calling delegate
        return delegate.process(state, move);
    }

    abstract public void processMovement(final State state, final GameMove move);

}
