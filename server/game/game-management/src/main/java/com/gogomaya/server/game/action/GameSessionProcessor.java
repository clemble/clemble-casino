package com.gogomaya.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.locks.ReentrantLock;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.event.ClientEvent;
import com.gogomaya.event.Event;
import com.gogomaya.game.Game;
import com.gogomaya.game.GameAware;
import com.gogomaya.game.GameSession;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.GameState;
import com.gogomaya.game.construct.GameInitiation;
import com.gogomaya.game.event.client.surrender.SurrenderEvent;
import com.gogomaya.game.event.server.GameServerEvent;
import com.gogomaya.game.event.server.GameStartedEvent;
import com.gogomaya.server.game.cache.GameCache;
import com.gogomaya.server.game.cache.GameCacheService;
import com.gogomaya.server.game.notification.TableServerRegistry;
import com.gogomaya.server.player.notification.PlayerNotificationService;

public class GameSessionProcessor<State extends GameState> implements GameAware {

    /**
     * Generated 27/07/13
     */
    private static final long serialVersionUID = 1688637028016268305L;

    final private Game game;

    final private TableServerRegistry serverRegistry;
    final private GameCacheService<State> cacheService;
    final private GameSessionFactory<State> sessionFactory;
    final private PlayerNotificationService<Event> notificationService;

    public GameSessionProcessor(
            final Game game,
            final TableServerRegistry serverRegistry,
            final GameSessionFactory<State> sessionFactory,
            final GameCacheService<State> cacheService,
            final PlayerNotificationService<Event> notificationService) {
        this.game = game;
        this.serverRegistry = checkNotNull(serverRegistry);
        this.notificationService = checkNotNull(notificationService);
        this.sessionFactory = checkNotNull(sessionFactory);
        this.cacheService = checkNotNull(cacheService);
    }

    @Override
    public Game getGame() {
        return game;
    }

    public GameSession<State> start(GameInitiation initiation) {
        // Step 1. Allocating table for game initiation
        final GameSession<State> session = sessionFactory.construct(initiation);
        // Step 2. Sending notification for game started
        notificationService.notify(initiation.getParticipants(), new GameStartedEvent<State>(session, serverRegistry.findServer(session)));
        // Step 3. Returning active table
        return session;
    }

    public State process(GameSessionKey session, ClientEvent move) {
        // Step 1. Sanity check
        if (move == null)
            throw GogomayaException.fromError(GogomayaError.GamePlayMoveUndefined);
        // Step 2. Acquiring lock for session event processing
        GameCache<State> cache = cacheService.get(session);
        // Step 3. Checking
        switch (cache.getSession().getSessionState()) {
        case finished:
            if (!(move instanceof SurrenderEvent)) {
                throw GogomayaException.fromError(GogomayaError.GamePlayGameEnded);
            }
            return cache.getSession().getState();
        default:
            break;
        }
        ReentrantLock reentrantLock = cache.getSessionLock();
        // Step 3. Acquiring lock for the session, to exclude parallel processing
        reentrantLock.lock();
        try {
            // Step 4. Retrieving game processor based on session identifier
            GameProcessor<State> processor = cache.getProcessor();
            // Step 5. Processing movement
            GameServerEvent<State> event = processor.process(cache.getSession(), move);
            // Step 6. Invoking appropriate notification
            notificationService.notify(cache.getPlayerIds(), event);
            // Step 7. Returning state of the game
            return cache.getSession().getState();
        } finally {
            reentrantLock.unlock();
        }
    }

}
