package com.gogomaya.server.game.action.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.event.GameEvent;

abstract public class AbstractGameProcessor<State extends GameState> implements GameProcessor<State> {

    final private GameProcessor<State> delegate;

    public AbstractGameProcessor(final GameProcessor<State> delegate) {
        this.delegate = checkNotNull(delegate);
    }

    @Override
    final public Collection<GameEvent<State>> process(final GameSession<State> session, final GameMove move) {
        // Step 1. Invoking process movement
        beforeMove(session, move);
        // Step 2. Calling delegate
        Collection<GameEvent<State>> events = delegate.process(session, move);
        // Step 3. Post processing after move was made
        if (events.size() != 0)
            afterMove(session, events);
        // Step 4. returning processed events
        return events;
    }

    abstract public void beforeMove(final GameSession<State> session, final GameMove move);

    abstract public Collection<GameEvent<State>> afterMove(final GameSession<State> session, final Collection<GameEvent<State>> events);

}
