package com.clemble.casino.server.game.action.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.action.MadeMove;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.game.action.GameProcessor;
import com.clemble.casino.server.game.action.GameProcessorFactory;
import com.clemble.casino.server.game.action.GameStateFactory;

abstract public class AbstractGameStateFactory<State extends GameState> implements GameStateFactory<State> {

    final private GameProcessorFactory<State> processorFactory;

    protected AbstractGameStateFactory(GameProcessorFactory<State> processorFactory) {
        this.processorFactory = checkNotNull(processorFactory);
    }

    @Override
    public State constructState(final GameSession<State> session) {
        // Step 1. Sanity check
        if (session == null || session.getSpecification() == null) {
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameStateReCreationFailure);
        }
        // Step 2. Re creating state
        GameInitiation initiation = session.toInitiation();
        // TODO define politics for restart, all time track is lost here
        GameContext context = new GameContext(initiation);
        State restoredState = constructState(initiation, context);
        GameProcessor<State> processor = processorFactory.create(initiation, context);
        // Step 2.1 To prevent population of original session with duplicated events
        GameSession<State> tmpSession = new GameSession<State>();
        tmpSession.setState(restoredState);
        for (MadeMove madeMove : MadeMove.sort(session.getMadeMoves())) {
            processor.process(tmpSession, madeMove.getMove());
        }
        // Step 3. Returning restored application state
        return restoredState;
    }

}