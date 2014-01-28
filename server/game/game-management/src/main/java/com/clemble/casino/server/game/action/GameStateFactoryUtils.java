package com.clemble.casino.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.action.MadeMove;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.construct.ServerGameInitiation;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.server.repository.game.ServerGameConfigurationRepository;

public class GameStateFactoryUtils<State extends GameState> {

    final private GameProcessorFactory<State> processorFactory;
    final private GameStateFactory<State> stateFactory;
    final private ServerGameConfigurationRepository configurationRepository;

    public GameStateFactoryUtils(GameProcessorFactory<State> processorFactory, GameStateFactory<State> stateFactory, ServerGameConfigurationRepository configurationRepository) {
        this.stateFactory = checkNotNull(stateFactory);
        this.processorFactory = checkNotNull(processorFactory);
        this.configurationRepository = checkNotNull(configurationRepository);
    }

    public State constructState(final GameSession<State> session) {
        // Step 1. Sanity check
        if (session == null || session.getConfigurationKey() == null) {
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameStateReCreationFailure);
        }
        // Step 2. Re creating state
        GameInitiation initiation = new GameInitiation(session.getSession(), session.getPlayers(), (MatchGameConfiguration) configurationRepository.findOne(session.getConfigurationKey()).getConfiguration());
        // TODO define politics for restart, all time track is lost here
        GameContext context = new GameContext(initiation, (MatchGameConfiguration) initiation.getConfiguration());
        State restoredState = stateFactory.constructState(initiation, context);
        ServerGameInitiation gameInitiation = new ServerGameInitiation(session.getSession(), context, (MatchGameConfiguration) initiation.getConfiguration());
        GameProcessor<State> processor = processorFactory.create(gameInitiation);
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
