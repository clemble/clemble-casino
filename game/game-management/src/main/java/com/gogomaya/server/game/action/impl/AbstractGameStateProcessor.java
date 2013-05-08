package com.gogomaya.server.game.action.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameStateCache;
import com.gogomaya.server.game.action.GameStateProcessor;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.event.GameEvent;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

abstract public class AbstractGameStateProcessor<State extends GameState> implements GameStateProcessor<State> {

    final private LoadingCache<Long, ReentrantLock> sessionLocks = CacheBuilder.newBuilder().build(new CacheLoader<Long, ReentrantLock>() {
        @Override
        public ReentrantLock load(Long key) throws Exception {
            return new ReentrantLock();
        }
    });

    final private GameStateCache<State> stateCache;

    protected AbstractGameStateProcessor(final GameStateCache<State> stateCache) {
        this.stateCache = checkNotNull(stateCache);
    }

    @Override
    public GameEvent<State> process(long sessionId, GameMove move) {
        // Step 1. Sanity check
        if (move == null)
            throw GogomayaException.create(GogomayaError.GamePlayMoveUndefined);
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
            State state = stateCache.getStateForSession(sessionId);
            final long playerId = move.getPlayerId();
            // Step 5. Checking that move
            GameMove expectedMove = state.getNextMove(playerId);
            if (expectedMove == null)
                throw GogomayaException.create(GogomayaError.GamePlayNoMoveExpected);
            if (expectedMove.getClass() != move.getClass())
                throw GogomayaException.create(GogomayaError.GamePlayWrongMoveType);
            // Step 6. Processing Select cell move
            resultEvent = apply(state, move);
            resultEvent.setSession(sessionId);
        } finally {
            reentrantLock.unlock();
        }
        return resultEvent;
    }

    abstract protected GameEvent<State> apply(State gameState, GameMove move);

}
