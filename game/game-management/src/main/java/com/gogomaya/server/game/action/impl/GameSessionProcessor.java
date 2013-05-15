package com.gogomaya.server.game.action.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.action.GameProcessorFactory;
import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameStateFactory;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.connection.GameConnection;
import com.gogomaya.server.game.connection.GameNotificationService;
import com.gogomaya.server.game.event.GameEvent;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.table.GameTableRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GameSessionProcessor<State extends GameState> {

    final private LoadingCache<Long, ReentrantLock> sessionLocks = CacheBuilder.newBuilder().build(new CacheLoader<Long, ReentrantLock>() {
        @Override
        public ReentrantLock load(Long key) throws Exception {
            return new ReentrantLock();
        }
    });

    final private LoadingCache<Long, State> sessionToStateCache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES)
            .build(new CacheLoader<Long, State>() {

                @Override
                public State load(Long sessionId) throws Exception {
                    // Step 1. Searching for appropriate session in repository
                    GameSession<State> session = sessionRepository.findOne(sessionId);
                    // Step 2. Creating new StateFactory based on retrieved session
                    return stateFactory.create(session);
                }

            });

    final private LoadingCache<Long, GameProcessor<State>> sessionToProcessorCache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES)
            .build(new CacheLoader<Long, GameProcessor<State>>() {

                @Override
                public GameProcessor<State> load(Long sessionId) throws Exception {
                    // Step 1. Searching for appropriate session in repository
                    GameSession<State> session = sessionRepository.findOne(sessionId);
                    // Step 2. Creating new StateFactory based on retrieved session
                    return processorFactory.create(session.getSpecification());
                }

            });

    final private LoadingCache<Long, GameConnection> sessionToConnection = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES)
            .build(new CacheLoader<Long, GameConnection>() {

                @Override
                public GameConnection load(Long sessionId) throws Exception {
                    // Step 1. Retrieving associated table
                    GameTable<State> table = tableRepository.findBySessionId(sessionId);
                    // Step 2. Generating appropriate GameConnection
                    return new GameConnection()
                        .setRoutingKey(table.getTableId())
                        .setServerConnection(table.getServerResource());
                }

            });

    final private GameStateFactory<State> stateFactory;
    final private GameProcessorFactory<State> processorFactory;
    final private GameNotificationService<State> notificationService;
    final private GameTableRepository<State> tableRepository;
    final private GameSessionRepository<State> sessionRepository;

    public GameSessionProcessor(final GameProcessorFactory<State> processorFactory,
            final GameStateFactory<State> stateFactory,
            final GameSessionRepository<State> sessionRepository,
            final GameTableRepository<State> tableRepository,
            final GameNotificationService<State> notificationService) {
        this.stateFactory = checkNotNull(stateFactory);
        this.sessionRepository = checkNotNull(sessionRepository);
        this.tableRepository = checkNotNull(tableRepository);
        this.processorFactory = checkNotNull(processorFactory);
        this.notificationService = checkNotNull(notificationService);
    }

    public State process(long sessionId, GameMove move) {
        // Step 1. Sanity check
        if (move == null)
            throw GogomayaException.create(GogomayaError.GamePlayMoveUndefined);
        // Step 2. Acquiring lock for session event processing
        ReentrantLock reentrantLock = null;
        // Step 3. Acquiring lock for the session, to exclude parallel processing
        try {
            reentrantLock = sessionLocks.get(sessionId);
            reentrantLock.lock();
            // Step 4. Recreating state if needed
            State state = sessionToStateCache.get(sessionId);
            // Step 5. Retrieving game processor based on session identifier
            GameProcessor<State> processor = sessionToProcessorCache.get(sessionId);
            // Step 6. Processing movement
            Collection<GameEvent<State>> events = processor.process(state, move);
            for (GameEvent<State> event : events)
                event.setSession(sessionId);
            // Step 7. Invoking appropriate notification
            notificationService.notify(sessionToConnection.get(sessionId), events);
            // Step 8. Returning state of the game
            return state;
        } catch (ExecutionException executionException) {
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        } finally {
            reentrantLock.unlock();
        }
    }

}
