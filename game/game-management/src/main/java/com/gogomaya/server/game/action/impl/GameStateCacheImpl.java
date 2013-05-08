package com.gogomaya.server.game.action.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameStateCache;
import com.gogomaya.server.game.action.GameStateFactory;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GameStateCacheImpl<State extends GameState> implements GameStateCache<State> {

    final private LoadingCache<Long, State> sessionToStateCache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES)
            .build(new CacheLoader<Long, State>() {

                @Override
                public State load(Long sessionId) throws Exception {
                    // Step 1. Searching for appropriate session in repository
                    GameSession session = sessionRepository.findOne(sessionId);
                    // Step 2. Creating new StateFactory based on retrieved session
                    return stateFactory.create(session);
                }

            });

    final private GameStateFactory<State> stateFactory;

    final private GameSessionRepository sessionRepository;

    public GameStateCacheImpl(final GameStateFactory<State> stateFactory, final GameSessionRepository sessionRepository) {
        this.sessionRepository = checkNotNull(sessionRepository);
        this.stateFactory = checkNotNull(stateFactory);
    }

    @Override
    public State getStateForSession(long sessionId) {
        try {
            return sessionToStateCache.get(sessionId);
        } catch (ExecutionException e) {
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }
    }

    @Override
    public void updateState(long sessionId, State newState) {
        sessionToStateCache.put(sessionId, newState);
    }

    @Override
    public void cleanState(long sessionId) {
        sessionToStateCache.invalidate(sessionId);
    }

}
