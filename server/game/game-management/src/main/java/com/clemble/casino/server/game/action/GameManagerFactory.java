package com.clemble.casino.server.game.action;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.OptimisticLockException;

import com.clemble.casino.game.*;
import com.clemble.casino.game.construction.GameInitiation;
import com.clemble.casino.game.event.MatchStartedEvent;
import com.clemble.casino.game.event.RoundStartedEvent;
import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.game.configuration.MatchGameConfiguration;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import com.clemble.casino.game.configuration.TournamentGameConfiguration;
import com.clemble.casino.server.game.aspect.ServerGameManagerFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.game.repository.GameRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameManagerFactory {

    final private Logger LOG = LoggerFactory.getLogger(GameManagerFactory.class);

    final private ConcurrentHashMap<String, GameManager<?>> sessionToManager = new ConcurrentHashMap<>();
    
    final private GameStateFactoryFacade stateFactory;
    final private ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext> roundManagerFactory;
    final private ServerGameManagerFactory<MatchGameConfiguration, MatchGameContext> matchManagerFactory;
    final private ServerGameManagerFactory<TournamentGameConfiguration, TournamentGameContext> tournamentAspectFactory;
    final private GameRecordRepository recordRepository;
    final private PlayerNotificationService notificationService;

    public GameManagerFactory(
            GameStateFactoryFacade stateFactory,
            ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext> roundGameManagerFactory,
            ServerGameManagerFactory<MatchGameConfiguration, MatchGameContext> matchGameManagerFactory,
            ServerGameManagerFactory<TournamentGameConfiguration, TournamentGameContext> tournamentGameManagerFactory,
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
    public <GC extends GameContext> GameManager<GC> get(String sessionKey) {
        GameManager<GC> gameManager = (GameManager<GC>) sessionToManager.get(sessionKey);
        if(gameManager == null)
            LOG.warn("{} can't find in sessionToManager mapping {}", sessionKey, hashCode());
        return gameManager;
    }

    public GameManager<?> start(GameInitiation initiation, GameContext<?> parent) {
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

    public GameManager<RoundGameContext> round(GameInitiation initiation, GameContext<?> parent) {
        RoundGameConfiguration roundConfiguration = (RoundGameConfiguration) initiation.getConfiguration();
        RoundGameContext roundGameContext = RoundGameContext.fromInitiation(initiation, parent);
        // Step 1. Allocating table for game initiation
        RoundGameState state = stateFactory.constructState(initiation, roundGameContext);
        // Step 2. Saving game record
        GameRecord roundRecord = initiation.toRecord();
        roundRecord = recordRepository.save(roundRecord);
        LOG.debug("{} saved round record {}", initiation.getSessionKey(), hashCode());
        // Step 3. Constructing manager and saving in a session
        GameManager<RoundGameContext> roundManager = roundManagerFactory.create(state, roundConfiguration, roundGameContext);
        sessionToManager.put(initiation.getSessionKey(), roundManager);
        LOG.debug("{} created and stored round manager {}", initiation.getSessionKey(), hashCode());
        // Step 4. Sending round started event
        RoundStartedEvent<RoundGameState> startedEvent = new RoundStartedEvent<RoundGameState>(initiation.getSessionKey(), state);
        notificationService.notify(initiation.getParticipants(), startedEvent);
        LOG.debug("{} sent notification for initiation {}", initiation.getSessionKey(), hashCode());
        return roundManager;
    }

    // TODO make this internal to the system, with no available processing from outside
    public GameManager<MatchGameContext> match(GameInitiation initiation, GameContext<?> parent) {
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
        GameManager<MatchGameContext> matchGameManager = matchManagerFactory.create(gameProcessor, configuration, context);
        sessionToManager.put(initiation.getSessionKey(), matchGameManager);
        // Step 4. Generating match started event
        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(initiation.getSessionKey(), context);
        notificationService.notify(initiation.getParticipants(), matchStartedEvent);
        // Step 5. Processing match started event
        matchGameManager.process(matchStartedEvent);
        return matchGameManager;
    }

    public GameManager<TournamentGameContext> tournament(GameInitiation initiation, GameContext<?> parent) {
        return null;
    }

}
