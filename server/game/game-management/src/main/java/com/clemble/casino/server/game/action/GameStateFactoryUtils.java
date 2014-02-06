package com.clemble.casino.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.MatchGameRecord;
import com.clemble.casino.game.action.MadeMove;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.server.repository.game.ServerGameConfigurationRepository;

public class GameStateFactoryUtils {

    final private MatchGameProcessorFactory processorFactory;
    final private GameStateFactory stateFactory;
    final private ServerGameConfigurationRepository configurationRepository;

    public GameStateFactoryUtils(MatchGameProcessorFactory processorFactory, GameStateFactory stateFactory, ServerGameConfigurationRepository configurationRepository) {
        this.stateFactory = checkNotNull(stateFactory);
        this.processorFactory = checkNotNull(processorFactory);
        this.configurationRepository = checkNotNull(configurationRepository);
    }

    public GameState constructState(final MatchGameRecord session) {
        // Step 1. Sanity check
        if (session == null || session.getConfigurationKey() == null) {
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameStateReCreationFailure);
        }
        // Step 2. Re creating state
        GameInitiation initiation = new GameInitiation(session.getSession(), session.getPlayers(), (MatchGameConfiguration) configurationRepository.findOne(session.getConfigurationKey()).getConfiguration());
        // TODO define politics for restart, all time track is lost here
        MatchGameContext context = new MatchGameContext(initiation);
        GameState restoredState = stateFactory.constructState(initiation, context);
        MatchGameProcessor processor = processorFactory.create((MatchGameConfiguration) initiation.getConfiguration(), context);
        // Step 2.1 To prevent population of original session with duplicated events
        MatchGameRecord tmpSession = new MatchGameRecord();
        tmpSession.setState(restoredState);
        for (MadeMove madeMove : MadeMove.sort(session.getMadeMoves())) {
            processor.process(tmpSession, madeMove.getMove());
        }
        // Step 3. Returning restored application state
        return restoredState;
    }

}
