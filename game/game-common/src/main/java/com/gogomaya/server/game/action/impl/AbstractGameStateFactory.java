package com.gogomaya.server.game.action.impl;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameStateFactory;
import com.gogomaya.server.game.action.move.MadeMove;

abstract public class AbstractGameStateFactory<State extends GameState> implements GameStateFactory<State> {

    @Override
    public State create(GameSession gameSession) {
        // Step 1. Sanity check
        if(gameSession == null || gameSession.getSpecification() == null) {
            throw GogomayaException.create(GogomayaError.GameStateReCreationFailure);
        }
        // Step 2. Re creating state
        State restoredState = create(gameSession.getSpecification(), gameSession.getPlayers());
        for(MadeMove madeMove: MadeMove.sort(gameSession.getMadeMoves())) {
           restoredState.apply(madeMove.getMove());
        }
        // Step 3. Returning restored application state
        return restoredState;
    }
}
