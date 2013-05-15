package com.gogomaya.server.game.action;

import java.util.concurrent.ExecutionException;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.connection.GameConnection;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.table.GameTableRepository;
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
            State state = stateFactory.create(session);
            session.setState(state);
            // Step 3. Creating new StateFactory based on retrieved session
            GameProcessor<State> processor = processorFactory.create(session.getSpecification());
            // Step 4. Retrieving associated table
            GameTable<State> table = tableRepository.findBySessionId(sessionId);
            // Step 5. Generating appropriate GameConnection
            GameConnection connection = new GameConnection().setRoutingKey(table.getTableId()).setServerConnection(table.getServerResource());

            return new GameCache<State>(session, state, processor, connection);
        }
    });

    final private GameStateFactory<State> stateFactory;
    final private GameProcessorFactory<State> processorFactory;
    final private GameTableRepository<State> tableRepository;
    final private GameSessionRepository<State> sessionRepository;

    public GameCacheService(final GameSessionRepository<State> sessionRepository,
            GameTableRepository<State> tableRepository,
            GameProcessorFactory<State> processorFactory,
            GameStateFactory<State> stateFactory) {
        this.stateFactory = stateFactory;
        this.processorFactory = processorFactory;
        this.sessionRepository = sessionRepository;
        this.tableRepository = tableRepository;
    }

    public GameCache<State> get(Long sessionId) {
        try {
            return gameCache.get(sessionId);
        } catch (ExecutionException e) {
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }
    }
}
