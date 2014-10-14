package com.clemble.casino.server.game.action;

import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import com.clemble.casino.game.lifecycle.initiation.GameInitiation;
import com.clemble.casino.game.lifecycle.management.GameContext;
import com.clemble.casino.game.lifecycle.management.GamePlayerContext;
import com.clemble.casino.game.lifecycle.management.MatchGameContext;
import com.clemble.casino.game.lifecycle.management.MatchGameState;
import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.game.lifecycle.management.event.MatchStartedEvent;
import com.clemble.casino.game.lifecycle.record.GameRecord;
import com.clemble.casino.server.action.ClembleManager;
import com.clemble.casino.server.action.ClembleManagerFactory;
import com.clemble.casino.server.game.repository.GameRecordRepository;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

/**
 * Created by mavarazy on 10/9/14.
 */
public class MatchManagerFactory implements GameManagerFactory {

    final private GameManagerFactoryFacade managerFactory;
    final private RoundStateFactoryFacade stateFactory;
    final private ClembleManagerFactory<MatchGameConfiguration> matchManagerFactory;
    final private GameRecordRepository recordRepository;
    final private PlayerNotificationService notificationService;

    public MatchManagerFactory(
            GameManagerFactoryFacade managerFactory,
            RoundStateFactoryFacade stateFactory,
            ClembleManagerFactory<MatchGameConfiguration> matchGameManagerFactory,
            GameRecordRepository recordRepository,
            PlayerNotificationService notificationService) {
        this.managerFactory = checkNotNull(managerFactory);
        this.stateFactory = checkNotNull(stateFactory);
        this.recordRepository = checkNotNull(recordRepository);
        this.notificationService = checkNotNull(notificationService);
        this.matchManagerFactory = checkNotNull(matchGameManagerFactory);
    }

    // TODO make this internal to the system, with no available processing from outside
    public ClembleManager<GameManagementEvent, MatchGameState> start(GameInitiation initiation, GameContext<?> parent) {
        MatchGameContext context = MatchGameContext.fromInitiation(initiation, parent);
        // Step 1. Fetching first pot configuration
        MatchGameConfiguration matchConfiguration = (MatchGameConfiguration) initiation.getConfiguration();
        long guaranteedPotSize = matchConfiguration.getPrice().getAmount();
        for(GameConfiguration configuration: matchConfiguration.getConfigurations())
            guaranteedPotSize -= configuration.getPrice().getAmount();
        if (guaranteedPotSize > 0) {
            for(GamePlayerContext playerContext: context.getPlayerContexts()) {
                context.add(guaranteedPotSize);
                playerContext.getAccount().subLeft(guaranteedPotSize);
            }
        }
        // Step 2. Generating new pot game record
        GameRecord matchGameRecord = initiation.toRecord();
        // Step 3. Saving match record
        matchGameRecord = recordRepository.save(matchGameRecord);
        // Step 3. Generating game manager
        MatchGameConfiguration configuration = (MatchGameConfiguration) initiation.getConfiguration();
        MatchGameState gameProcessor = new MatchGameState(context, configuration, null, 0);
        ClembleManager<GameManagementEvent, MatchGameState> matchGameManager = matchManagerFactory.create(gameProcessor, configuration);
        return matchGameManager;
    }

}
