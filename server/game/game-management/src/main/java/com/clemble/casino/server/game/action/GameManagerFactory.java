package com.clemble.casino.server.game.action;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.OptimisticLockException;

import com.clemble.casino.game.*;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.MatchStartedEvent;
import com.clemble.casino.game.event.server.RoundStartedEvent;
import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.game.configuration.MatchGameConfiguration;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import com.clemble.casino.game.configuration.TournamentGameConfiguration;
import com.clemble.casino.server.game.aspect.ServerGameManagerFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.game.repository.GameRecordRepository;
import com.clemble.casino.server.game.repository.ServerGameConfigurationRepository;

public class GameManagerFactory {

    final private ConcurrentHashMap<String, GameManager<?>> sessionToManager = new ConcurrentHashMap<>();
    
    final private GameStateFactoryFacade stateFactory;
    final private ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext> roundManagerFactory;
    final private ServerGameManagerFactory<MatchGameConfiguration, MatchGameContext> matchManagerFactory;
    final private ServerGameManagerFactory<TournamentGameConfiguration, TournamentGameContext> tournamentAspectFactory;
    final private GameRecordRepository recordRepository;
    final private PlayerNotificationService notificationService;
    final private ServerGameConfigurationRepository configurationRepository;

    public GameManagerFactory(
            GameStateFactoryFacade stateFactory,
            ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext> roundGameManagerFactory,
            ServerGameManagerFactory<MatchGameConfiguration, MatchGameContext> matchGameManagerFactory,
            ServerGameManagerFactory<TournamentGameConfiguration, TournamentGameContext> tournamentGameManagerFactory,
            GameRecordRepository roundRepository,
            ServerGameConfigurationRepository configurationRepository,
            PlayerNotificationService notificationService) {
        this.stateFactory = checkNotNull(stateFactory);
        this.recordRepository = checkNotNull(roundRepository);
        this.notificationService = checkNotNull(notificationService);
        this.configurationRepository = checkNotNull(configurationRepository);
        this.matchManagerFactory = checkNotNull(matchGameManagerFactory);
        this.roundManagerFactory = checkNotNull(roundGameManagerFactory);
        this.tournamentAspectFactory = checkNotNull(tournamentGameManagerFactory);
    }

    @SuppressWarnings("unchecked")
    public <GC extends GameContext> GameManager<GC> get(String sessionKey) {
        return (GameManager<GC>) sessionToManager.get(sessionKey);
    }

    public GameManager<?> start(GameInitiation initiation, GameContext<?> parent) {
        try {
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
        GameRecord roundRecord = new GameRecord()
            .setSessionKey(initiation.getSessionKey())
            .setConfiguration(initiation.getConfiguration().getConfigurationKey())
            .setSessionState(GameSessionState.active)
            .setPlayers(initiation.getParticipants());
        roundRecord = recordRepository.save(roundRecord);
        // Step 3. Constructing manager and saving in a session
        GameManager<RoundGameContext> roundManager = roundManagerFactory.create(state, roundConfiguration, roundGameContext);
        sessionToManager.put(initiation.getSessionKey(), roundManager);
        // Step 4. Sending round started event
        RoundStartedEvent<RoundGameState> startedEvent = new RoundStartedEvent<RoundGameState>(initiation.getSessionKey(), state);
        notificationService.notify(initiation.getParticipants(), startedEvent);
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
        GameRecord matchGameRecord = new GameRecord()
            .setSessionKey(initiation.getSessionKey())
            .setConfiguration(initiation.getConfiguration().getConfigurationKey())
            .setSessionState(GameSessionState.active)
            .setPlayers(initiation.getParticipants());
        // Step 3. Saving match record
        matchGameRecord = recordRepository.save(matchGameRecord);
        // Step 3. Generating game manager
        MatchGameConfiguration configuration = (MatchGameConfiguration) initiation.getConfiguration();
        MatchGameState gameProcessor = new MatchGameState(context, configuration, this);
        GameManager<MatchGameContext> matchGameManager = matchManagerFactory.create(gameProcessor, configuration, context);
        // Step 4. Generating match started event
        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(initiation.getSessionKey(), context);
        notificationService.notify(initiation.getParticipants(), matchStartedEvent);
        // Step 5. Processing match started event
        matchGameManager.process(matchStartedEvent);
        sessionToManager.put(initiation.getSessionKey(), matchGameManager);
        return matchGameManager;
    }

    public GameManager<TournamentGameContext> tournament(GameInitiation initiation, GameContext<?> parent) {
        return null;
    }

}
