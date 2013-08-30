package com.gogomaya.server.game.cache;

import com.gogomaya.game.GameSession;
import com.gogomaya.game.GameState;
import com.gogomaya.game.SessionAware;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameInitiation;
import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.action.GameProcessorFactory;
import com.gogomaya.server.game.action.GameStateFactory;
import com.gogomaya.server.repository.game.GameConstructionRepository;
import com.gogomaya.server.repository.game.GameSessionRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GameCacheService<State extends GameState> {

    final private LoadingCache<Long, GameCache<State>> gameCache = CacheBuilder.newBuilder().build(new CacheLoader<Long, GameCache<State>>() {
        @Override
        public GameCache<State> load(Long sessionId) throws Exception {
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

    public GameCache<State> get(SessionAware sessionAware) {
        return gameCache.getUnchecked(sessionAware.getSession());
    }

    public GameCache<State> get(Long sessionId) {
        return gameCache.getUnchecked(sessionId);
    }
}
