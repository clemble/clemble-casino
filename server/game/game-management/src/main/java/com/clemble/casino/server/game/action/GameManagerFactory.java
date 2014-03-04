package com.clemble.casino.server.game.action;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.OptimisticLockException;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.*;
import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.MatchStartedEvent;
import com.clemble.casino.game.event.server.RoundStartedEvent;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.game.specification.RoundGameConfiguration;
import com.clemble.casino.game.specification.TournamentGameConfiguration;
import com.clemble.casino.server.game.aspect.ServerGameAspectFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.repository.game.RoundGameRecordRepository;
import com.clemble.casino.server.repository.game.MatchGameRecordRepository;
import com.clemble.casino.server.repository.game.ServerGameConfigurationRepository;

public class GameManagerFactory {

    final private ConcurrentHashMap<GameSessionKey, GameManager<?>> sessionToManager = new ConcurrentHashMap<>();
    
    final private GameStateFactoryFacade stateFactory;
    final private ServerGameAspectFactory<RoundGameConfiguration, RoundGameContext, RoundGameRecord> matchAspectFactory;
    final private ServerGameAspectFactory<MatchGameConfiguration, MatchGameContext, MatchGameRecord> potAspectFactory;
    final private ServerGameAspectFactory<TournamentGameConfiguration, TournamentGameContext, TournamentGameRecord> tournamentAspectFactory;
    final private MatchGameRecordRepository potRepository;
    final private RoundGameRecordRepository sessionRepository;
    final private PlayerNotificationService notificationService;
    final private ServerGameConfigurationRepository configurationRepository;

    public GameManagerFactory(
            MatchGameRecordRepository potRepository,
            GameStateFactoryFacade stateFactory,
            ServerGameAspectFactory<RoundGameConfiguration, RoundGameContext, RoundGameRecord> matchProcessorFactory,
            ServerGameAspectFactory<MatchGameConfiguration, MatchGameContext, MatchGameRecord> potProcessorFactory,
            ServerGameAspectFactory<TournamentGameConfiguration, TournamentGameContext, TournamentGameRecord> tournamentAspectFactory,
            RoundGameRecordRepository sessionRepository,
            ServerGameConfigurationRepository configurationRepository,
            PlayerNotificationService notificationService) {
        this.stateFactory = checkNotNull(stateFactory);
        this.potRepository = checkNotNull(potRepository);
        this.sessionRepository = checkNotNull(sessionRepository);
        this.notificationService = checkNotNull(notificationService);
        this.configurationRepository = checkNotNull(configurationRepository);
        this.potAspectFactory = checkNotNull(potProcessorFactory);
        this.matchAspectFactory = checkNotNull(matchProcessorFactory);
        this.tournamentAspectFactory = checkNotNull(tournamentAspectFactory);
    }

    @SuppressWarnings("unchecked")
    public <R extends GameRecord> GameManager<R> get(GameSessionKey sessionKey) {
        //return (GameManager<R>) sessionToManager.getUnchecked(sessionKey);
        return (GameManager<R>) sessionToManager.get(sessionKey);
    }

    public GameManager<?> start(GameInitiation initiation, GameContext<?> parent) {
        try {
            if (initiation.getConfiguration() instanceof RoundGameConfiguration) {
                return match(initiation, parent);
            } else if (initiation.getConfiguration() instanceof MatchGameConfiguration) {
                return pot(initiation, parent);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (OptimisticLockException lockException) {
            // TODO Be really careful with this shit
            return start(initiation, parent);
        }
    }

    public GameManager<RoundGameRecord> match(GameInitiation initiation, GameContext<?> parent) {
        RoundGameConfiguration matchGameConfiguration = (RoundGameConfiguration) initiation.getConfiguration();
        // Step 1. Allocating table for game initiation
        GameState state = stateFactory.constructState(initiation, new RoundGameContext(initiation));
        // Step 2. Sending notification for game started
        RoundGameContext context = new RoundGameContext(initiation, parent);
        GameProcessor<RoundGameRecord, Event> processor = matchAspectFactory.create(state, matchGameConfiguration, new RoundGameContext(initiation, parent));
        // Step 3. Returning active table
        RoundGameRecord matchRecord = new RoundGameRecord()
            .setSession(initiation.getSession())
            .setConfiguration(initiation.getConfiguration().getConfigurationKey())
            .setSessionState(GameSessionState.active)
            .setPlayers(initiation.getParticipants()).setState(state);
        matchRecord = sessionRepository.saveAndFlush(matchRecord);
        GameManager<RoundGameRecord> manager = new GameManager<>(processor, matchRecord, context);
        sessionToManager.put(initiation.getSession(), manager);
        notificationService.notify(initiation.getParticipants(), new RoundStartedEvent<GameState>(initiation.getSession(), state));
        return manager;
    }

    // TODO make this internal to the system, with no available processing from outside
    public GameManager<MatchGameRecord> pot(GameInitiation initiation, GameContext<?> parent) {
        MatchGameContext context = new MatchGameContext(initiation, parent);
        // Step 1. Fetching first pot configuration
        MatchGameConfiguration potConfiguration = (MatchGameConfiguration) initiation.getConfiguration();
        long guaranteedPotSize = potConfiguration.getPrice().getAmount();
        for(GameConfiguration configuration: potConfiguration.getConfigurations())
            guaranteedPotSize -= configuration.getPrice().getAmount();
        if (guaranteedPotSize > 0) {
            for(GamePlayerContext playerContext: context.getPlayerContexts()) {
                context.add(guaranteedPotSize);
                playerContext.getAccount().subLeft(guaranteedPotSize);
            }
        }
        // Step 2. Taking first match from the pot
        GameConfiguration subConfiguration = potConfiguration.getConfigurations().get(0);
        // Step 3. Constructing match initiation
        GameInitiation subInitiation = new GameInitiation(initiation.getSession().append("0"), subConfiguration, initiation.getParticipants());
        // Step 4. Sending notification to related players
        // Step 5. Generating new match game record
        GameManager<?> subManager = start(subInitiation, context);
        context.setCurrentSession(subManager.getContext().getSession());
        // Step 6. Generating new pot game record
        MatchGameRecord matchGameRecord = new MatchGameRecord(initiation.getSession(), initiation.getConfiguration().getConfigurationKey(), GameSessionState.active, Collections.<GameSessionKey>emptyList());
        matchGameRecord.getSubRecords().add(subManager.getRecord().getSession());
        // Step 7. Saving pot record
        matchGameRecord = potRepository.saveAndFlush(matchGameRecord);
        MatchGameConfiguration configuration = (MatchGameConfiguration) initiation.getConfiguration();
        MatchGameProcessor gameProcessor = new MatchGameProcessor(context, configuration, this);
        GameProcessor<MatchGameRecord, Event> potProcessor = potAspectFactory.create(gameProcessor, configuration, context);
        GameManager<MatchGameRecord> potGameManager = new GameManager<>(potProcessor, matchGameRecord, context);
        sessionToManager.put(initiation.getSession(), potGameManager);
        notificationService.notify(initiation.getParticipants(), new MatchStartedEvent(initiation.getSession(), context));
        // Step 8. Returning pot game record
        return potGameManager;
    }

    public GameManager<TournamentGameRecord> tournament(GameInitiation initiation, GameContext<?> parent) {
        return null;
    }

}
