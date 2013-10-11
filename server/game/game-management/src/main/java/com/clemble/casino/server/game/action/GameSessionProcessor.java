package com.clemble.casino.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.locks.ReentrantLock;

import com.clemble.casino.error.GogomayaError;
import com.clemble.casino.error.GogomayaException;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameAware;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.client.surrender.SurrenderEvent;
import com.clemble.casino.game.event.server.GameServerEvent;
import com.clemble.casino.game.event.server.GameStartedEvent;
import com.clemble.casino.server.game.cache.GameCache;
import com.clemble.casino.server.game.cache.GameCacheService;
import com.clemble.casino.server.game.notification.TableServerRegistry;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

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
