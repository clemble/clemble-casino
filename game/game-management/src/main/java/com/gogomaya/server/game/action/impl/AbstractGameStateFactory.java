package com.gogomaya.server.game.action.impl;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.action.GameProcessorFactory;
import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameStateFactory;
import com.gogomaya.server.game.action.move.MadeMove;

abstract public class AbstractGameStateFactory<State extends GameState> implements GameStateFactory<State> {

    final private GameProcessorFactory<State> processorFactory;

    protected AbstractGameStateFactory(GameProcessorFactory<State> processorFactory) {
        this.processorFactory = processorFactory;
    }

    @Override
    public State create(GameSession<State> session) {
        // Step 1. Sanity check
        if (session == null || session.getSpecification() == null) {
            throw GogomayaException.create(GogomayaError.GameStateReCreationFailure);
        }
        // Step 2. Re creating state
        State restoredState = create(session.getSpecification(), session.getPlayers());
        GameProcessor<State> processor = processorFactory.create(session.getSpecification());
        for (MadeMove madeMove : MadeMove.sort(session.getMadeMoves())) {
            processor.process(session, restoredState, madeMove.getMove());
        }
        // Step 3. Returning restored application state
        return restoredState;
    }

}