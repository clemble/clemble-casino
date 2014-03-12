package com.clemble.casino.server.game.action;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.OptimisticLockException;

import com.clemble.casino.game.*;
import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.MatchStartedEvent;
import com.clemble.casino.game.event.server.RoundStartedEvent;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.game.specification.RoundGameConfiguration;
import com.clemble.casino.game.specification.TournamentGameConfiguration;
import com.clemble.casino.server.game.aspect.ServerGameManagerFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.repository.game.GameRecordRepository;
import com.clemble.casino.server.repository.game.ServerGameConfigurationRepository;

public class GameManagerFactory {

    final private ConcurrentHashMap<GameSessionKey, GameManager<?>> sessionToManager = new ConcurrentHashMap<>();
    
    final private GameStateFactoryFacade stateFactory;
    final private ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext> roundManagerFactory;
    final private ServerGameManagerFactory<MatchGameConfiguration, MatchGameContext> matchManagerFactory;
    final private ServerGameManagerFactory<TournamentGameConfiguration, TournamentGameContext> tournamentAspectFactory;
    final private GameRecordRepository roundRepository;
    final private PlayerNotificationService notificationService;
    final private ServerGameConfigurationRepository configurationRepository;

    public GameManagerFactory(
            GameStateFactoryFacade stateFactory,
            ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext> matchProcessorFactory,
            ServerGameManagerFactory<MatchGameConfiguration, MatchGameContext> potProcessorFactory,
            ServerGameManagerFactory<TournamentGameConfiguration, TournamentGameContext> tournamentAspectFactory,
            GameRecordRepository roundRepository,
            ServerGameConfigurationRepository configurationRepository,
            PlayerNotificationService notificationService) {
        this.stateFactory = checkNotNull(stateFactory);
        this.roundRepository = checkNotNull(roundRepository);
        this.notificationService = checkNotNull(notificationService);
        this.configurationRepository = checkNotNull(configurationRepository);
        this.matchManagerFactory = checkNotNull(potProcessorFactory);
        this.roundManagerFactory = checkNotNull(matchProcessorFactory);
        this.tournamentAspectFactory = checkNotNull(tournamentAspectFactory);
    }

    @SuppressWarnings("unchecked")
    public <GC extends GameContext> GameManager<GC> get(GameSessionKey sessionKey) {
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
        RoundGameConfiguration matchGameConfiguration = (RoundGameConfiguration) initiation.getConfiguration();
        // Step 1. Allocating table for game initiation
        RoundGameState state = stateFactory.constructState(initiation, new RoundGameContext(initiation));
        // Step 2. Sending notification for game started
        RoundGameContext context = new RoundGameContext(initiation, parent);
        // Step 3. Returning active table
        GameRecord roundRecord = new GameRecord()
            .setSession(initiation.getSession())
            .setConfiguration(initiation.getConfiguration().getConfigurationKey())
            .setSessionState(GameSessionState.active)
            .setPlayers(initiation.getParticipants());
        roundRecord = roundRepository.saveAndFlush(roundRecord);
        GameManager<RoundGameContext> manager = roundManagerFactory.create(state, matchGameConfiguration, new RoundGameContext(initiation, parent));
        sessionToManager.put(initiation.getSession(), manager);
        notificationService.notify(initiation.getParticipants(), new RoundStartedEvent<RoundGameState>(initiation.getSession(), state));
        return manager;
    }

    // TODO make this internal to the system, with no available processing from outside
    public GameManager<MatchGameContext> match(GameInitiation initiation, GameContext<?> parent) {
        MatchGameContext context = new MatchGameContext(initiation, parent);
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
            .setSession(initiation.getSession())
            .setConfiguration(initiation.getConfiguration().getConfigurationKey())
            .setSessionState(GameSessionState.active)
            .setPlayers(initiation.getParticipants());
        // TODO make this part of the initiation process matchGameRecord.getSubRecords().add(subManager.getRecord().getSession());
        // Step 3. Saving pot record
        matchGameRecord = roundRepository.saveAndFlush(matchGameRecord);
        MatchGameConfiguration configuration = (MatchGameConfiguration) initiation.getConfiguration();
        MatchGameState gameProcessor = new MatchGameState(context, configuration, this);
        GameManager<MatchGameContext> potGameManager = matchManagerFactory.create(gameProcessor, configuration, context);
        sessionToManager.put(initiation.getSession(), potGameManager);
        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(initiation.getSession(), context);
        notificationService.notify(initiation.getParticipants(), matchStartedEvent);
        // Step 8. Returning pot game record
        potGameManager.process(matchStartedEvent);
        return potGameManager;
    }

    public GameManager<TournamentGameContext> tournament(GameInitiation initiation, GameContext<?> parent) {
        return null;
    }

}
