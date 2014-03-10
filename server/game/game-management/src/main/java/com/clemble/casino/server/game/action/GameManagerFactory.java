package com.clemble.casino.server.game.action;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collections;
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
import com.clemble.casino.server.repository.game.RoundGameRecordRepository;
import com.clemble.casino.server.repository.game.MatchGameRecordRepository;
import com.clemble.casino.server.repository.game.ServerGameConfigurationRepository;

public class GameManagerFactory {

    final private ConcurrentHashMap<GameSessionKey, GameManager<?>> sessionToManager = new ConcurrentHashMap<>();
    
    final private GameStateFactoryFacade stateFactory;
    final private ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext, RoundGameRecord> roundManagerFactory;
    final private ServerGameManagerFactory<MatchGameConfiguration, MatchGameContext, MatchGameRecord> matchManagerFactory;
    final private ServerGameManagerFactory<TournamentGameConfiguration, TournamentGameContext, TournamentGameRecord> tournamentAspectFactory;
    final private MatchGameRecordRepository potRepository;
    final private RoundGameRecordRepository sessionRepository;
    final private PlayerNotificationService notificationService;
    final private ServerGameConfigurationRepository configurationRepository;

    public GameManagerFactory(
            MatchGameRecordRepository potRepository,
            GameStateFactoryFacade stateFactory,
            ServerGameManagerFactory<RoundGameConfiguration, RoundGameContext, RoundGameRecord> matchProcessorFactory,
            ServerGameManagerFactory<MatchGameConfiguration, MatchGameContext, MatchGameRecord> potProcessorFactory,
            ServerGameManagerFactory<TournamentGameConfiguration, TournamentGameContext, TournamentGameRecord> tournamentAspectFactory,
            RoundGameRecordRepository sessionRepository,
            ServerGameConfigurationRepository configurationRepository,
            PlayerNotificationService notificationService) {
        this.stateFactory = checkNotNull(stateFactory);
        this.potRepository = checkNotNull(potRepository);
        this.sessionRepository = checkNotNull(sessionRepository);
        this.notificationService = checkNotNull(notificationService);
        this.configurationRepository = checkNotNull(configurationRepository);
        this.matchManagerFactory = checkNotNull(potProcessorFactory);
        this.roundManagerFactory = checkNotNull(matchProcessorFactory);
        this.tournamentAspectFactory = checkNotNull(tournamentAspectFactory);
    }

    @SuppressWarnings("unchecked")
    public <GC extends GameContext> GameManager<GC> get(GameSessionKey sessionKey) {
        //return (GameManager<R>) sessionToManager.getUnchecked(sessionKey);
        return (GameManager<GC>) sessionToManager.get(sessionKey);
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

    public GameManager<RoundGameContext> match(GameInitiation initiation, GameContext<?> parent) {
        RoundGameConfiguration matchGameConfiguration = (RoundGameConfiguration) initiation.getConfiguration();
        // Step 1. Allocating table for game initiation
        RoundGameState state = stateFactory.constructState(initiation, new RoundGameContext(initiation));
        // Step 2. Sending notification for game started
        RoundGameContext context = new RoundGameContext(initiation, parent);
        // Step 3. Returning active table
        RoundGameRecord roundRecord = new RoundGameRecord()
            .setSession(initiation.getSession())
            .setConfiguration(initiation.getConfiguration().getConfigurationKey())
            .setSessionState(GameSessionState.active)
            .setPlayers(initiation.getParticipants());
        roundRecord = sessionRepository.saveAndFlush(roundRecord);
        GameManager<RoundGameContext> manager = roundManagerFactory.create(state, matchGameConfiguration, new RoundGameContext(initiation, parent));
        sessionToManager.put(initiation.getSession(), manager);
        notificationService.notify(initiation.getParticipants(), new RoundStartedEvent<RoundGameState>(initiation.getSession(), state));
        return manager;
    }

    // TODO make this internal to the system, with no available processing from outside
    public GameManager<MatchGameContext> pot(GameInitiation initiation, GameContext<?> parent) {
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
        // TODO make this part of the initiation process matchGameRecord.getSubRecords().add(subManager.getRecord().getSession());
        // Step 7. Saving pot record
        matchGameRecord = potRepository.saveAndFlush(matchGameRecord);
        MatchGameConfiguration configuration = (MatchGameConfiguration) initiation.getConfiguration();
        MatchGameState gameProcessor = new MatchGameState(context, configuration, this);
        GameManager<MatchGameContext> potGameManager = matchManagerFactory.create(gameProcessor, configuration, context);
        sessionToManager.put(initiation.getSession(), potGameManager);
        notificationService.notify(initiation.getParticipants(), new MatchStartedEvent(initiation.getSession(), context));
        // Step 8. Returning pot game record
        return potGameManager;
    }

    public GameManager<TournamentGameContext> tournament(GameInitiation initiation, GameContext<?> parent) {
        return null;
    }

}
