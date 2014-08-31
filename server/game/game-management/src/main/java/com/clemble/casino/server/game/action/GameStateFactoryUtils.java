package com.clemble.casino.server.game.action;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameRecord;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.action.GameEventRecord;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import com.clemble.casino.server.game.aspect.ServerGameManagerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameStateFactoryUtils {

    final private GameStateFactoryFacade stateFactory;
    final private ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext> processorFactory;

    public GameStateFactoryUtils(ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext> processorFactory, GameStateFactoryFacade stateFactoryFacade) {
        this.stateFactory = checkNotNull(stateFactoryFacade);
        this.processorFactory = checkNotNull(processorFactory);
    }

    public GameState constructState(final GameRecord record) {
        // Step 1. Sanity check
        if (record == null || record.getConfiguration() == null) {
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameStateReCreationFailure);
        }
        // Step 2. Re creating state
        GameInitiation initiation = new GameInitiation(record.getSessionKey(), record.getPlayers(), record.getConfiguration());
        // TODO define politics for restart, all time track is lost here
        RoundGameContext context = RoundGameContext.fromInitiation(initiation, null);
        GameState restoredState = stateFactory.constructState(initiation, context);
        // Step 2.1 To prevent population of original session with duplicated events
        for (GameEventRecord eventRecord : record.getEventRecords()) {
            if(!(eventRecord.getEvent() instanceof GameManagementEvent))
                restoredState.process(eventRecord.getEvent());
        }
        // Step 3. Returning restored application state
        return restoredState;
    }

}
