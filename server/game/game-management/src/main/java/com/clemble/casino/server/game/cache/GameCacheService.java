package com.clemble.casino.server.game.cache;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.MatchGameRecord;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.construct.ServerGameInitiation;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.server.game.action.GameProcessor;
import com.clemble.casino.server.game.action.GameProcessorFactory;
import com.clemble.casino.server.game.action.GameStateFactory;
import com.clemble.casino.server.repository.game.GameSessionRepository;
import com.clemble.casino.server.repository.game.ServerGameConfigurationRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GameCacheService<State extends GameState> {

    final private LoadingCache<GameSessionKey, GameCache<State>> gameCache = CacheBuilder.newBuilder().build(new CacheLoader<GameSessionKey, GameCache<State>>() {
        @Override
        public GameCache<State> load(GameSessionKey sessionId) throws Exception {
            try {
                // Step 1. Searching for appropriate session in repository
                MatchGameRecord<State> session = sessionRepository.findOne(sessionId);
                // Step 2. Creating appropriate initiation
                GameInitiation initiation = new GameInitiation(session.getSession(), session.getPlayers(), (MatchGameConfiguration) configurationRepository.findOne(session.getConfigurationKey()).getConfiguration());
                // Step 3. Construction appropriate GameCache
                return construct(session, initiation);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                throw new RuntimeException(throwable);
            }
        }
    });

    final private GameStateFactory<State> stateFactory;
    final private GameProcessorFactory<State> processorFactory;
    final private GameSessionRepository<State> sessionRepository;
    final private ServerGameConfigurationRepository configurationRepository;

    public GameCacheService(final GameSessionRepository<State> sessionRepository,
            final GameProcessorFactory<State> processorFactory,
            final GameStateFactory<State> stateFactory,
            final ServerGameConfigurationRepository configurationRepository) {
        this.stateFactory = checkNotNull(stateFactory);
        this.processorFactory = checkNotNull(processorFactory);
        this.sessionRepository = checkNotNull(sessionRepository);
        this.configurationRepository = checkNotNull(configurationRepository);
    }

    public GameCache<State> get(GameSessionAware sessionAware) {
        return gameCache.getUnchecked(sessionAware.getSession());
    }

    public GameCache<State> get(GameSessionKey sessionId) {
        return gameCache.getUnchecked(sessionId);
    }

    public GameCache<State> construct(MatchGameRecord<State> session, GameInitiation initiation) {
        try {
            GameConfiguration configuration = initiation.getConfiguration();
            GameContext context = new GameContext(initiation, (MatchGameConfiguration) configuration);
            ServerGameInitiation gameInitiation = new ServerGameInitiation(session.getSession(), context, (MatchGameConfiguration) initiation.getConfiguration());
            GameProcessor<State> processor = processorFactory.create(gameInitiation);
            session.setState(stateFactory.constructState(initiation, context));
            // Step 4. Retrieving associated table
            return new GameCache<State>(session, processor, session.getPlayers());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new RuntimeException(throwable);
        }
    }
}
