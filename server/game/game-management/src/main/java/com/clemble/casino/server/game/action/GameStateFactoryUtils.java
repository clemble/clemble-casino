package com.clemble.casino.server.game.action;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.RoundGameRecord;
import com.clemble.casino.game.action.MadeMove;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.specification.RoundGameConfiguration;
import com.clemble.casino.server.game.aspect.ServerGameManagerFactory;
import com.clemble.casino.server.repository.game.ServerGameConfigurationRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameStateFactoryUtils {

    final private GameStateFactoryFacade stateFactory;
    final private ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext, RoundGameRecord> processorFactory;
    final private ServerGameConfigurationRepository configurationRepository;

    public GameStateFactoryUtils(ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext, RoundGameRecord> processorFactory, GameStateFactoryFacade stateFactoryFacade, ServerGameConfigurationRepository configurationRepository) {
        this.stateFactory = checkNotNull(stateFactoryFacade);
        this.processorFactory = checkNotNull(processorFactory);
        this.configurationRepository = checkNotNull(configurationRepository);
    }

    public GameState constructState(final RoundGameRecord session) {
        // Step 1. Sanity check
        if (session == null || session.getConfigurationKey() == null) {
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameStateReCreationFailure);
        }
        // Step 2. Re creating state
        GameInitiation initiation = new GameInitiation(session.getSession(), session.getPlayers(), (RoundGameConfiguration) configurationRepository.findOne(session.getConfigurationKey()).getConfiguration());
        // TODO define politics for restart, all time track is lost here
        RoundGameContext context = new RoundGameContext(initiation);
        GameState restoredState = stateFactory.constructState(initiation, context);
        // Step 2.1 To prevent population of original session with duplicated events
        RoundGameRecord tmpSession = new RoundGameRecord();
        for (MadeMove madeMove : MadeMove.sort(session.getMadeMoves())) {
            restoredState.process(madeMove.getMove());
        }
        // Step 3. Returning restored application state
        return restoredState;
    }

}
