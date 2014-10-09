package com.clemble.casino.server.game.action;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.OptimisticLockException;

import com.clemble.casino.game.lifecycle.initiation.GameInitiation;
import com.clemble.casino.game.lifecycle.management.*;
import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.game.lifecycle.management.event.MatchStartedEvent;
import com.clemble.casino.game.lifecycle.management.event.RoundStartedEvent;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.TournamentGameConfiguration;
import com.clemble.casino.game.lifecycle.record.GameRecord;
import com.clemble.casino.server.action.ClembleManager;
import com.clemble.casino.server.action.ClembleManagerFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.game.repository.GameRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameManagerFactory {

    final private Logger LOG = LoggerFactory.getLogger(GameManagerFactory.class);

    final private ConcurrentHashMap<String, ClembleManager<GameManagementEvent, ? extends GameState>> sessionToManager = new ConcurrentHashMap<>();
    
    final private GameStateFactoryFacade stateFactory;
    final private ClembleManagerFactory<RoundGameConfiguration> roundManagerFactory;
    final private ClembleManagerFactory<MatchGameConfiguration> matchManagerFactory;
    final private ClembleManagerFactory<TournamentGameConfiguration> tournamentAspectFactory;
    final private GameRecordRepository recordRepository;
    final private PlayerNotificationService notificationService;

    public GameManagerFactory(
            GameStateFactoryFacade stateFactory,
            ClembleManagerFactory<RoundGameConfiguration> roundGameManagerFactory,
            ClembleManagerFactory<MatchGameConfiguration> matchGameManagerFactory,
            ClembleManagerFactory<TournamentGameConfiguration> tournamentGameManagerFactory,
            GameRecordRepository recordRepository,
            PlayerNotificationService notificationService) {
        this.stateFactory = checkNotNull(stateFactory);
        this.recordRepository = checkNotNull(recordRepository);
        this.notificationService = checkNotNull(notificationService);
        this.matchManagerFactory = checkNotNull(matchGameManagerFactory);
        this.roundManagerFactory = checkNotNull(roundGameManagerFactory);
        this.tournamentAspectFactory = checkNotNull(tournamentGameManagerFactory);
    }

    @SuppressWarnings("unchecked")
    public <GC extends GameContext> ClembleManager<GameManagementEvent, ? extends GameState> get(String sessionKey) {
        ClembleManager<GameManagementEvent, ? extends GameState> gameManager = sessionToManager.get(sessionKey);
        if(gameManager == null)
            LOG.warn("{} can't find in sessionToManager mapping {}", sessionKey, hashCode());
        return gameManager;
    }

    public ClembleManager<GameManagementEvent, ?> start(GameInitiation initiation, GameContext<?> parent) {
        try {
            LOG.debug("{} starting", initiation.getSessionKey());
            if (initiation.getConfiguration() instanceof RoundGameConfiguration) {
                return round(initiation, parent);
            } else if (initiation.getConfiguration() instanceof MatchGameConfiguration) {
                return match(initiation, parent);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (OptimisticLockException lockException) {
            // TODO Be really careful with this shit
            return start(initiation, parent);
        }
    }

    public ClembleManager<GameManagementEvent, RoundGameState> round(GameInitiation initiation, GameContext<?> parent) {
        RoundGameConfiguration roundConfiguration = (RoundGameConfiguration) initiation.getConfiguration();
        RoundGameContext roundGameContext = RoundGameContext.fromInitiation(initiation, parent);
        // Step 1. Allocating table for game initiation
        RoundGameState state = stateFactory.constructState(initiation, roundGameContext);
        // Step 2. Saving game record
        GameRecord roundRecord = initiation.toRecord();
        roundRecord = recordRepository.save(roundRecord);
        LOG.debug("{} saved round record {}", initiation.getSessionKey(), hashCode());
        // Step 3. Constructing manager and saving in a session
        ClembleManager<GameManagementEvent, RoundGameState> roundManager = roundManagerFactory.create(state, roundConfiguration);
        sessionToManager.put(initiation.getSessionKey(), roundManager);
        LOG.debug("{} created and stored round manager {}", initiation.getSessionKey(), hashCode());
        // Step 4. Sending round started event
        RoundStartedEvent<RoundGameState> startedEvent = new RoundStartedEvent<RoundGameState>(initiation.getSessionKey(), state);
        notificationService.notify(initiation.getParticipants(), startedEvent);
        LOG.debug("{} sent notification for initiation {}", initiation.getSessionKey(), hashCode());
        return roundManager;
    }

    // TODO make this internal to the system, with no available processing from outside
    public ClembleManager<GameManagementEvent, MatchGameState> match(GameInitiation initiation, GameContext<?> parent) {
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
        MatchGameState gameProcessor = new MatchGameState(context, configuration, this);
        ClembleManager<GameManagementEvent, MatchGameState> matchGameManager = matchManagerFactory.create(gameProcessor, configuration);
        sessionToManager.put(initiation.getSessionKey(), matchGameManager);
        // Step 4. Generating match started event
        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(initiation.getSessionKey(), context);
        notificationService.notify(initiation.getParticipants(), matchStartedEvent);
        // Step 5. Processing match started event
        matchGameManager.process(matchStartedEvent);
        return matchGameManager;
    }

    public ClembleManager<GameManagementEvent, TournamentGameState> tournament(GameInitiation initiation, GameContext<?> parent) {
        return null;
    }

}
