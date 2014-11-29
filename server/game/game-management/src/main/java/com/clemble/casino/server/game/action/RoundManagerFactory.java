package com.clemble.casino.server.game.action;

import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
import com.clemble.casino.game.lifecycle.initiation.GameInitiation;
import com.clemble.casino.game.lifecycle.management.GameContext;
import com.clemble.casino.game.lifecycle.management.RoundGameContext;
import com.clemble.casino.game.lifecycle.management.RoundGameState;
import com.clemble.casino.game.lifecycle.management.RoundState;
import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.game.lifecycle.record.GameRecord;
import com.clemble.casino.server.action.ClembleManager;
import com.clemble.casino.server.action.ClembleManagerFactory;
import com.clemble.casino.server.game.repository.GameRecordRepository;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mavarazy on 10/9/14.
 */
public class RoundManagerFactory implements GameManagerFactory {

    final private Logger LOG = LoggerFactory.getLogger(GameManagerFactoryFacade.class);

    final private RoundStateFactoryFacade stateFactory;
    final private GameRecordRepository recordRepository;
    final private ClembleManagerFactory<RoundGameConfiguration> roundManagerFactory;

    public RoundManagerFactory(
        RoundStateFactoryFacade stateFactory,
        ClembleManagerFactory<RoundGameConfiguration> roundManagerFactory,
        GameRecordRepository recordRepository,
        ServerNotificationService notificationService) {
        this.stateFactory = stateFactory;
        this.roundManagerFactory = roundManagerFactory;
        this.recordRepository = recordRepository;
    }

     public ClembleManager<GameManagementEvent, RoundGameState> start(GameInitiation initiation, GameContext<?> parent) {
        RoundGameConfiguration roundConfiguration = (RoundGameConfiguration) initiation.getConfiguration();
        RoundGameContext roundGameContext = RoundGameContext.fromInitiation(initiation, parent);
        // Step 1. Allocating table for game initiation
        RoundState roundState = stateFactory.constructState(initiation, roundGameContext);
        RoundGameState state = new RoundGameState(roundConfiguration, roundGameContext, roundState, 0);
        // Step 2. Saving game record
        GameRecord roundRecord = initiation.toRecord();
        roundRecord = recordRepository.save(roundRecord);
        LOG.debug("{} saved round record {}", initiation.getSessionKey(), hashCode());
        // Step 3. Constructing manager and saving in a session
        ClembleManager<GameManagementEvent, RoundGameState> roundManager = roundManagerFactory.create(state, roundConfiguration);
        LOG.debug("{} created and stored round manager {}", initiation.getSessionKey(), hashCode());
        return roundManager;
    }

}
