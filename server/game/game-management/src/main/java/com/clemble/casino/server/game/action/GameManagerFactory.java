package com.clemble.casino.server.game.action;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameRecord;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameSessionState;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.MatchGameRecord;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.PotGameRecord;
import com.clemble.casino.game.PotPlayerGameContext;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.construct.ServerGameInitiation;
import com.clemble.casino.game.event.server.GameMatchStartedEvent;
import com.clemble.casino.game.event.server.GamePotStartedEvent;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.repository.game.MatchGameRecordRepository;
import com.clemble.casino.server.repository.game.PotGameRecordRepository;
import com.clemble.casino.server.repository.game.ServerGameConfigurationRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GameManagerFactory {

    final private LoadingCache<GameSessionKey, GameManager<?>> sessionToManager = CacheBuilder.newBuilder().build(
            new CacheLoader<GameSessionKey, GameManager<?>>() {
                @Override
                public GameManager<?> load(GameSessionKey key) throws Exception {
                    // Step 1. Searching for appropriate session in repository
                    MatchGameRecord<?> session = sessionRepository.findOne(key);
                    // Step 2. Creating appropriate initiation
                    GameConfiguration configuration = configurationRepository.findOne(session.getConfigurationKey()).getConfiguration();
                    // Step 3. Constructing initiation
                    GameInitiation initiation = new GameInitiation(session.getSession(), session.getPlayers(), configuration);
                    // Step 4. To match
                    return start(initiation, null);
                }
            });

    final private PotGameRecordRepository potRepository;
    final private GameStateFactory<GameState> stateFactory;
    final private MatchGameProcessorFactory<GameState> processorFactory;
    final private MatchGameRecordRepository<GameState> sessionRepository;
    final private PlayerNotificationService notificationService;
    final private ServerGameConfigurationRepository configurationRepository;

    public GameManagerFactory(PotGameRecordRepository potRepository,
            GameStateFactory<GameState> stateFactory,
            MatchGameProcessorFactory<GameState> processorFactory,
            MatchGameRecordRepository<GameState> sessionRepository,
            ServerGameConfigurationRepository configurationRepository,
            PlayerNotificationService notificationService) {
        this.stateFactory = checkNotNull(stateFactory);
        this.potRepository = checkNotNull(potRepository);
        this.processorFactory = checkNotNull(processorFactory);
        this.sessionRepository = checkNotNull(sessionRepository);
        this.notificationService = checkNotNull(notificationService);
        this.configurationRepository = checkNotNull(configurationRepository);
    }

    public <R extends GameRecord> GameManager<R> get(GameSessionKey sessionKey) {
        return (GameManager<R>) sessionToManager.getUnchecked(sessionKey);
    }

    public GameManager<?> start(GameInitiation initiation, GameContext parent) {
        if (initiation.getConfiguration() instanceof MatchGameConfiguration) {
            return match(initiation, parent);
        } else if (initiation.getConfiguration() instanceof PotGameConfiguration) {
            return pot(initiation, parent);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public MatchGameManager<?> match(GameInitiation initiation, GameContext parent) {
        MatchGameConfiguration matchGameConfiguration = (MatchGameConfiguration) initiation.getConfiguration();
        // Step 1. Allocating table for game initiation
        GameState state = stateFactory.constructState(initiation, new MatchGameContext(initiation));
        MatchGameRecord<GameState> matchRecord = new MatchGameRecord<GameState>().setSession(initiation.getSession())
                .setConfiguration(initiation.getConfiguration().getConfigurationKey()).setSessionState(GameSessionState.active)
                .setPlayers(initiation.getParticipants()).setState(state);
        matchRecord = sessionRepository.saveAndFlush(matchRecord);
        // Step 2. Sending notification for game started
        ServerGameInitiation serverInitiation = new ServerGameInitiation(initiation.getSession(), new MatchGameContext(initiation), matchGameConfiguration);
        MatchGameProcessor<GameState> processor = processorFactory.create(serverInitiation);
        // Step 3. Returning active table
        MatchGameManager<?> manager = new MatchGameManager<>(processor, matchRecord);
        sessionToManager.put(initiation.getSession(), manager);
        notificationService.notify(initiation.getParticipants(), new GameMatchStartedEvent<GameState>(matchRecord));
        return manager;
    }

    public PotGameManager pot(GameInitiation initiation, GameContext parent) {
        // Step 1. Fetching first pot configuration
        PotGameConfiguration potConfiguration = (PotGameConfiguration) initiation.getConfiguration();
        // Step 2. Taking first match from the pot
        MatchGameConfiguration matchConfiguration = potConfiguration.getMatchConfigurations().get(0);
        // Step 3. Constructing match initiation
        GameInitiation matchInitiation = new GameInitiation(initiation.getSession().append("0"), matchConfiguration, initiation.getParticipants());
        // Step 4. Generating new match game record
        PotGameContext potGameContext = new PotGameContext(Collections.<PotPlayerGameContext>emptyList(), null, 0);
        MatchGameManager<?> matchRecord = match(matchInitiation, potGameContext);
        // Step 5. Generating new pot game record
        List<MatchGameRecord<?>> matchRecords = new ArrayList<>();
        matchRecords.add(matchRecord.getRecord());
        PotGameRecord potGameRecord = new PotGameRecord(initiation.getSession(), initiation.getConfiguration().getConfigurationKey(), GameSessionState.active, matchRecords);
        // Step 6. Saving pot record
        potGameRecord = potRepository.saveAndFlush(potGameRecord);
        PotGameManager potGameManager = new PotGameManager(potGameRecord);
        sessionToManager.put(initiation.getSession(), potGameManager);
        // Step 7. Sending notification to related players
        notificationService.notify(initiation.getParticipants(), new GamePotStartedEvent(potGameRecord.getSession()));
        // Step 8. Returning pot game record
        return potGameManager;
    }

}
