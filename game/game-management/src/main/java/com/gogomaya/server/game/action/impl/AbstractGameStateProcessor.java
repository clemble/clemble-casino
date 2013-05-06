package com.gogomaya.server.game.action.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameStateFactory;
import com.gogomaya.server.game.action.GameStateProcessor;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.event.GameEvent;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

abstract public class AbstractGameStateProcessor<State extends GameState> implements GameStateProcessor<State> {

    final private GameStateFactory<State> stateFactory;

    final private GameSessionRepository sessionRepository;

    final private LoadingCache<Long, ReentrantLock> sessionLocks = CacheBuilder.newBuilder().build(new CacheLoader<Long, ReentrantLock>() {
        @Override
        public ReentrantLock load(Long key) throws Exception {
            return new ReentrantLock();
        }
    });

    final private ConcurrentHashMap<Long, State> stateCache = new ConcurrentHashMap<Long, State>();

    protected AbstractGameStateProcessor(GameStateFactory<State> stateFactory, GameSessionRepository sessionRepository) {
        this.stateFactory = checkNotNull(stateFactory);
        this.sessionRepository = checkNotNull(sessionRepository);
    }

    @Override
    public GameEvent<State> process(Long sessionId, GameMove move) {
        // Step 1. Sanity check
        if (move == null)
            throw GogomayaException.create(GogomayaError.GamePlayMoveUndefined);
        if (sessionId == null)
            throw GogomayaException.create(GogomayaError.ServerSessionProcessingError);
        GameEvent<State> resultEvent = null;
        // Step 2. Acquiring lock for session event processing
        ReentrantLock reentrantLock;
        try {
            reentrantLock = sessionLocks.get(sessionId);
        } catch (ExecutionException e) {
            throw GogomayaException.create(GogomayaError.ServerSessionProcessingError);
        }
        // Step 3. Acquiring lock for the session, to exclude parallel processing
        reentrantLock.lock();
        try {
            // Step 4. Recreating state if needed
            State state = stateCache.get(sessionId);
            if (state == null) {
                GameSession session = sessionRepository.findOne(sessionId);
                state = stateFactory.create(session);
                stateCache.put(sessionId, state);
            }
            final long playerId = move.getPlayerId();
            // Step 5. Checking that move
            GameMove expectedMove = state.getNextMove(playerId);
            if (expectedMove == null)
                throw GogomayaException.create(GogomayaError.GamePlayNoMoveExpected);
            if (expectedMove.getClass() != move.getClass())
                throw GogomayaException.create(GogomayaError.GamePlayWrongMoveType);
            // Step 6. Processing Select cell move
            resultEvent =  apply(state, move);
        } finally {
            reentrantLock.unlock();
        }
        return resultEvent;
    }

    abstract protected GameEvent<State> apply(State gameState, GameMove move);

}
