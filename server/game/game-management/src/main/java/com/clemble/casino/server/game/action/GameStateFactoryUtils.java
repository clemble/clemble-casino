package com.clemble.casino.server.game.action;

import com.clemble.casino.lifecycle.initiation.InitiationState;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.lifecycle.record.GameRecord;
import com.clemble.casino.game.lifecycle.management.GameState;
import com.clemble.casino.game.lifecycle.management.RoundGameContext;
import com.clemble.casino.game.lifecycle.initiation.GameInitiation;
import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
import com.clemble.casino.lifecycle.record.EventRecord;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.action.ClembleManagerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameStateFactoryUtils {

    final private GameStateFactoryFacade stateFactory;
    final private ClembleManagerFactory<RoundGameConfiguration> processorFactory;

    public GameStateFactoryUtils(ClembleManagerFactory<RoundGameConfiguration> processorFactory, GameStateFactoryFacade stateFactoryFacade) {
        this.stateFactory = checkNotNull(stateFactoryFacade);
        this.processorFactory = checkNotNull(processorFactory);
    }

    public GameState constructState(final GameRecord record) {
        // Step 1. Sanity check
        if (record == null || record.getConfiguration() == null) {
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameStateReCreationFailure, PlayerAware.DEFAULT_PLAYER);
        }
        // Step 2. Re creating state
        GameInitiation initiation = new GameInitiation(record.getSessionKey(), InitiationState.pending, record.getPlayers(), record.getConfiguration());
        // TODO define politics for restart, all time track is lost here
        RoundGameContext context = RoundGameContext.fromInitiation(initiation, null);
        GameState restoredState = stateFactory.constructState(initiation, context);
        // Step 2.1 To prevent population of original session with duplicated events
        for (EventRecord eventRecord : record.getEventRecords()) {
            if(!(eventRecord.getEvent() instanceof GameManagementEvent))
                restoredState.process(eventRecord.getEvent());
        }
        // Step 3. Returning restored application state
        return restoredState;
    }

}
