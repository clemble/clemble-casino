package com.gogomaya.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.locks.ReentrantLock;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.event.ClientEvent;
import com.gogomaya.event.Event;
import com.gogomaya.game.Game;
import com.gogomaya.game.GameAware;
import com.gogomaya.game.GameState;
import com.gogomaya.game.GameTable;
import com.gogomaya.game.construct.GameInitiation;
import com.gogomaya.game.event.client.surrender.SurrenderEvent;
import com.gogomaya.game.event.server.GameServerEvent;
import com.gogomaya.game.event.server.GameStartedEvent;
import com.gogomaya.server.game.cache.GameCache;
import com.gogomaya.server.game.cache.GameCacheService;
import com.gogomaya.server.player.notification.PlayerNotificationService;

public class GameSessionProcessor<State extends GameState> implements GameAware {

    /**
     * Generated 27/07/13
     */
    private static final long serialVersionUID = 1688637028016268305L;

    final private Game game;

    final private GameCacheService<State> cacheService;
    final private GameTableFactory<State> tableFactory;
    final private PlayerNotificationService<Event> notificationService;

    public GameSessionProcessor(final Game game,
            final GameTableFactory<State> tableFactory,
            final GameCacheService<State> cacheService,
            final PlayerNotificationService<Event> notificationService) {
        this.game = game;
        this.notificationService = checkNotNull(notificationService);
        this.tableFactory = checkNotNull(tableFactory);
        this.cacheService = checkNotNull(cacheService);
    }

    @Override
    public Game getGame() {
        return game;
    }

    public GameTable<State> start(GameInitiation initiation) {
        // Step 1. Allocating table for game initiation
        final GameTable<State> table = tableFactory.constructTable(initiation);
        // Step 2. Sending notification for game started
        notificationService.notify(initiation.getParticipants(), new GameStartedEvent<State>(initiation.getSession(), table));
        // Step 3. Returning active table
        return table;
    }

    public State process(long sessionId, ClientEvent move) {
        // Step 1. Sanity check
        if (move == null)
            throw GogomayaException.fromError(GogomayaError.GamePlayMoveUndefined);
        // Step 2. Acquiring lock for session event processing
        GameCache<State> cache = cacheService.get(sessionId);
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
            event.setSession(sessionId);
            // Step 6. Invoking appropriate notification
            notificationService.notify(cache.getPlayerIds(), event);
            // Step 7. Returning state of the game
            return cache.getSession().getState();
        } finally {
            reentrantLock.unlock();
        }
    }

}
