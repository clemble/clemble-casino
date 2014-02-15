package com.clemble.casino.server.game.action;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.OptimisticLockException;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameProcessor;
import com.clemble.casino.game.GameRecord;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameSessionState;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.MatchGameRecord;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.PotGameRecord;
import com.clemble.casino.game.TournamentGameContext;
import com.clemble.casino.game.TournamentGameRecord;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameMatchStartedEvent;
import com.clemble.casino.game.event.server.GamePotStartedEvent;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.game.specification.TournamentGameConfiguration;
import com.clemble.casino.server.game.aspect.ServerGameAspectFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.repository.game.MatchGameRecordRepository;
import com.clemble.casino.server.repository.game.PotGameRecordRepository;
import com.clemble.casino.server.repository.game.ServerGameConfigurationRepository;

public class GameManagerFactory {

    final private ConcurrentHashMap<GameSessionKey, GameManager<?>> sessionToManager = new ConcurrentHashMap<>();
    
//    final private LoadingCache<GameSessionKey, GameManager<?>> sessionToManager = CacheBuilder.newBuilder().build(
//            new CacheLoader<GameSessionKey, GameManager<?>>() {
//                @Override
//                public GameManager<?> load(GameSessionKey key) throws Exception {
//                    // Step 1. Searching for appropriate session in repository
//                    MatchGameRecord session = sessionRepository.findOne(key);
//                    // Step 2. Creating appropriate initiation
//                    GameConfiguration configuration = configurationRepository.findOne(session.getConfigurationKey()).getConfiguration();
//                    // Step 3. Constructing initiation
//                    GameInitiation initiation = new GameInitiation(session.getSession(), session.getPlayers(), configuration);
//                    // Step 4. To match
//                    return start(initiation, null);
//                }
//            });

    final private GameStateFactoryFacade stateFactory;
    final private ServerGameAspectFactory<MatchGameConfiguration, MatchGameContext, MatchGameRecord> matchAspectFactory;
    final private ServerGameAspectFactory<PotGameConfiguration, PotGameContext, PotGameRecord> potAspectFactory;
    final private ServerGameAspectFactory<TournamentGameConfiguration, TournamentGameContext, TournamentGameRecord> tournamentAspectFactory;
    final private PotGameRecordRepository potRepository;
    final private MatchGameRecordRepository sessionRepository;
    final private PlayerNotificationService notificationService;
    final private ServerGameConfigurationRepository configurationRepository;

    public GameManagerFactory(
            PotGameRecordRepository potRepository,
            GameStateFactoryFacade stateFactory,
            ServerGameAspectFactory<MatchGameConfiguration, MatchGameContext, MatchGameRecord> matchProcessorFactory,
            ServerGameAspectFactory<PotGameConfiguration, PotGameContext, PotGameRecord> potProcessorFactory,
            ServerGameAspectFactory<TournamentGameConfiguration, TournamentGameContext, TournamentGameRecord> tournamentAspectFactory,
            MatchGameRecordRepository sessionRepository,
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
            if (initiation.getConfiguration() instanceof MatchGameConfiguration) {
                return match(initiation, parent);
            } else if (initiation.getConfiguration() instanceof PotGameConfiguration) {
                return pot(initiation, parent);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (OptimisticLockException lockException) {
            // TODO Be really careful with this shit
            return start(initiation, parent);
        }
    }

    public GameManager<MatchGameRecord> match(GameInitiation initiation, GameContext<?> parent) {
        MatchGameConfiguration matchGameConfiguration = (MatchGameConfiguration) initiation.getConfiguration();
        // Step 1. Allocating table for game initiation
        GameState state = stateFactory.constructState(initiation, new MatchGameContext(initiation));
        // Step 2. Sending notification for game started
        GameProcessor<MatchGameRecord, Event> processor = matchAspectFactory.create(state, matchGameConfiguration, new MatchGameContext(initiation, parent));
        // Step 3. Returning active table
        MatchGameRecord matchRecord = new MatchGameRecord()
            .setSession(initiation.getSession())
            .setConfiguration(initiation.getConfiguration().getConfigurationKey())
            .setSessionState(GameSessionState.active)
            .setPlayers(initiation.getParticipants()).setState(state);
        matchRecord = sessionRepository.saveAndFlush(matchRecord);
        GameManager<MatchGameRecord> manager = new GameManager<>(processor, matchRecord);
        sessionToManager.put(initiation.getSession(), manager);
        notificationService.notify(initiation.getParticipants(), new GameMatchStartedEvent<GameState>(matchRecord));
        return manager;
    }

    public GameManager<PotGameRecord> pot(GameInitiation initiation, GameContext<?> parent) {
        PotGameContext potGameContext = new PotGameContext(initiation, parent);
        // Step 1. Fetching first pot configuration
        PotGameConfiguration potConfiguration = (PotGameConfiguration) initiation.getConfiguration();
        // Step 2. Taking first match from the pot
        GameConfiguration subConfiguration = potConfiguration.getConfigurations().get(0);
        // Step 3. Constructing match initiation
        GameInitiation subInitiation = new GameInitiation(initiation.getSession().append("0"), subConfiguration, initiation.getParticipants());
        // Step 4. Sending notification to related players
        notificationService.notify(initiation.getParticipants(), new GamePotStartedEvent(initiation.getSession()));
        // Step 5. Generating new match game record
        GameManager<?> subManager = start(subInitiation, potGameContext);
        // Step 6. Generating new pot game record
        PotGameRecord potGameRecord = new PotGameRecord(initiation.getSession(), initiation.getConfiguration().getConfigurationKey(), GameSessionState.active, Collections.<GameSessionKey>emptyList());
        potGameRecord.getSubRecords().add(subManager.getRecord().getSession());
        // Step 7. Saving pot record
        potGameRecord = potRepository.saveAndFlush(potGameRecord);
        PotGameContext context = new PotGameContext(initiation);
        PotGameConfiguration configuration = (PotGameConfiguration) initiation.getConfiguration();
        PotGameProcessor gameProcessor = new PotGameProcessor(context, configuration, this);
        GameProcessor<PotGameRecord, Event> potProcessor = potAspectFactory.create(gameProcessor, configuration, context);
        GameManager<PotGameRecord> potGameManager = new GameManager<>(potProcessor, potGameRecord);
        sessionToManager.put(initiation.getSession(), potGameManager);
        // Step 8. Returning pot game record
        return potGameManager;
    }

    public GameManager<TournamentGameRecord> tournament(GameInitiation initiation, GameContext<?> parent) {
//        TournamentGameContext potGameContext = new TournamentGameContext(initiation, parent);
//        // Step 1. Fetching first pot configuration
//        PotGameConfiguration potConfiguration = (PotGameConfiguration) initiation.getConfiguration();
//        // Step 2. Taking first match from the pot
//        GameConfiguration subConfiguration = potConfiguration.getConfigurations().get(0);
//        // Step 3. Constructing match initiation
//        GameInitiation subInitiation = new GameInitiation(initiation.getSession().append("0"), subConfiguration, initiation.getParticipants());
//        // Step 4. Sending notification to related players
//        notificationService.notify(initiation.getParticipants(), new GamePotStartedEvent(initiation.getSession()));
//        // Step 5. Generating new match game record
//        GameManager<?> subManager = start(subInitiation, potGameContext);
//        // Step 6. Generating new pot game record
//        PotGameRecord potGameRecord = new PotGameRecord(initiation.getSession(), initiation.getConfiguration().getConfigurationKey(), GameSessionState.active, Collections.<GameSessionKey>emptyList());
//        potGameRecord.getSubRecords().add(subManager.getRecord().getSession());
//        // Step 7. Saving pot record
//        potGameRecord = potRepository.saveAndFlush(potGameRecord);
//        PotGameContext context = new PotGameContext(initiation);
//        PotGameConfiguration configuration = (PotGameConfiguration) initiation.getConfiguration();
//        PotGameProcessor gameProcessor = new PotGameProcessor(context, configuration, this);
//        GameProcessor<PotGameRecord, Event> potProcessor = potAspectFactory.create(gameProcessor, configuration, context);
//        GameManager<PotGameRecord> potGameManager = new GameManager<>(potProcessor, potGameRecord);
//        sessionToManager.put(initiation.getSession(), potGameManager);
//        // Step 8. Returning pot game record
        return null;
    }

}
