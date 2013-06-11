package com.gogomaya.server.game.action.impl;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.action.GameProcessorFactory;
import com.gogomaya.server.game.action.GameStateFactory;
import com.gogomaya.server.game.event.client.MadeMove;

abstract public class AbstractGameStateFactory<State extends GameState> implements GameStateFactory<State> {

    final private GameProcessorFactory<State> processorFactory;

    protected AbstractGameStateFactory(GameProcessorFactory<State> processorFactory) {
        this.processorFactory = processorFactory;
    }

    @Override
    public void restore(GameSession<State> session) {
        // Step 1. Sanity check
        if (session == null || session.getSpecification() == null) {
            throw GogomayaException.create(GogomayaError.GameStateReCreationFailure);
        }
        // Step 2. Re creating state
        State restoredState = create(session.getSpecification(), session.getPlayers());
        GameProcessor<State> processor = processorFactory.create(session);
        // Step 2.1 To prevent population of original session with duplicated events
        GameSession<State> tmpSession = new GameSession<State>();
        for (MadeMove madeMove : MadeMove.sort(session.getMadeMoves())) {
            processor.process(tmpSession, madeMove.getMove());
        }
        // Step 3. Returning restored application state
        session.setState(restoredState);
    }

}