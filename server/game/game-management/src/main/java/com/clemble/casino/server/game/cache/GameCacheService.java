package com.clemble.casino.server.game.cache;

import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.game.action.GameProcessor;
import com.clemble.casino.server.game.action.GameProcessorFactory;
import com.clemble.casino.server.game.action.GameStateFactory;
import com.clemble.casino.server.repository.game.GameConstructionRepository;
import com.clemble.casino.server.repository.game.GameSessionRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GameCacheService<State extends GameState> {

    final private LoadingCache<GameSessionKey, GameCache<State>> gameCache = CacheBuilder.newBuilder().build(new CacheLoader<GameSessionKey, GameCache<State>>() {
        @Override
        public GameCache<State> load(GameSessionKey sessionId) throws Exception {
            GameConstruction construction = constructionRepository.findOne(sessionId);
            // Step 1. Searching for appropriate session in repository
            GameSession<State> session = sessionRepository.findOne(sessionId);
            // Step 2. Creating new StateFactory based on retrieved session
            session.setState(stateFactory.constructState(session));
            // Step 3. Creating new StateFactory based on retrieved session
            GameProcessor<State> processor = processorFactory.create(new GameInitiation(construction));
            // Step 4. Retrieving associated table
            return new GameCache<State>(session, processor, session.getPlayers());
        }
    });

    final private GameStateFactory<State> stateFactory;
    final private GameProcessorFactory<State> processorFactory;
    final private GameSessionRepository<State> sessionRepository;
    final private GameConstructionRepository constructionRepository;

    public GameCacheService(final GameConstructionRepository constructionRepository,
            final GameSessionRepository<State> sessionRepository,
            final GameProcessorFactory<State> processorFactory,
            final GameStateFactory<State> stateFactory) {
        this.constructionRepository = constructionRepository;
        this.stateFactory = stateFactory;
        this.processorFactory = processorFactory;
        this.sessionRepository = sessionRepository;
    }

    public GameCache<State> get(GameSessionAware sessionAware) {
        return gameCache.getUnchecked(sessionAware.getSession());
    }

    public GameCache<State> get(GameSessionKey sessionId) {
        return gameCache.getUnchecked(sessionId);
    }
}
