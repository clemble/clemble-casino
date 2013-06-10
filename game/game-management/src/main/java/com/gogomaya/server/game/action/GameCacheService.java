package com.gogomaya.server.game.action;

import java.util.concurrent.ExecutionException;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GameCacheService<State extends GameState> {

    final private LoadingCache<Long, GameCache<State>> gameCache = CacheBuilder.newBuilder().build(new CacheLoader<Long, GameCache<State>>() {
        @Override
        public GameCache<State> load(Long sessionId) throws Exception {
            // Step 1. Searching for appropriate session in repository
            GameSession<State> session = sessionRepository.findOne(sessionId);
            // Step 2. Creating new StateFactory based on retrieved session
            stateFactory.restore(session);
            // Step 3. Creating new StateFactory based on retrieved session
            GameProcessor<State> processor = processorFactory.create(session.getSpecification());
            // Step 4. Retrieving associated table
            return new GameCache<State>(session, processor, session.getPlayers());
        }
    });

    final private GameStateFactory<State> stateFactory;
    final private GameProcessorFactory<State> processorFactory;
    final private GameSessionRepository<State> sessionRepository;

    public GameCacheService(final GameSessionRepository<State> sessionRepository, GameProcessorFactory<State> processorFactory,
            GameStateFactory<State> stateFactory) {
        this.stateFactory = stateFactory;
        this.processorFactory = processorFactory;
        this.sessionRepository = sessionRepository;
    }

    public GameCache<State> get(SessionAware sessionAware) {
        return get(sessionAware.getSession());
    }

    public GameCache<State> get(Long sessionId) {
        try {
            return gameCache.get(sessionId);
        } catch (ExecutionException e) {
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }
    }
}
