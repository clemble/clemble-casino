package com.clemble.casino.server.game.action;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameRecord;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.action.GameEventRecord;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.specification.RoundGameConfiguration;
import com.clemble.casino.server.game.aspect.ServerGameManagerFactory;
import com.clemble.casino.server.game.repository.ServerGameConfigurationRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameStateFactoryUtils {

    final private GameStateFactoryFacade stateFactory;
    final private ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext> processorFactory;
    final private ServerGameConfigurationRepository configurationRepository;

    public GameStateFactoryUtils(ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext> processorFactory, GameStateFactoryFacade stateFactoryFacade, ServerGameConfigurationRepository configurationRepository) {
        this.stateFactory = checkNotNull(stateFactoryFacade);
        this.processorFactory = checkNotNull(processorFactory);
        this.configurationRepository = checkNotNull(configurationRepository);
    }

    public GameState constructState(final GameRecord session) {
        // Step 1. Sanity check
        if (session == null || session.getConfigurationKey() == null) {
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameStateReCreationFailure);
        }
        // Step 2. Re creating state
        GameInitiation initiation = new GameInitiation(session.getSessionKey(), session.getPlayers(), (RoundGameConfiguration) configurationRepository.findOne(session.getConfigurationKey()).getConfiguration());
        // TODO define politics for restart, all time track is lost here
        RoundGameContext context = RoundGameContext.fromInitiation(initiation, null);
        GameState restoredState = stateFactory.constructState(initiation, context);
        // Step 2.1 To prevent population of original session with duplicated events
        for (GameEventRecord eventRecord : session.getEventRecords()) {
            if(!(eventRecord.getEvent() instanceof GameManagementEvent))
                restoredState.process(eventRecord.getEvent());
        }
        // Step 3. Returning restored application state
        return restoredState;
    }

}
