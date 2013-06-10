package com.gogomaya.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.event.client.SurrenderEvent;
import com.gogomaya.server.game.event.server.GameEvent;
import com.gogomaya.server.game.notification.GameNotificationService;

public class GameSessionProcessor<State extends GameState> {

    final private GameCacheService<State> cacheService;
    final private GameNotificationService<State> notificationService;

    public GameSessionProcessor(final GameCacheService<State> cacheService,
            final GameNotificationService<State> notificationService) {
        this.notificationService = checkNotNull(notificationService);
        this.cacheService = checkNotNull(cacheService);
    }

    public State process(long sessionId, ClientEvent move) {
        // Step 1. Sanity check
        if (move == null)
            throw GogomayaException.create(GogomayaError.GamePlayMoveUndefined);
        // Step 2. Acquiring lock for session event processing
        GameCache<State> cache = cacheService.get(sessionId);
        // Step 3. Checking
        switch (cache.getSession().getSessionState()) {
        case inactive:
            if (!(move instanceof SurrenderEvent)) {
                throw GogomayaException.create(GogomayaError.GamePlayGameNotStarted);
            }
            break;
        case ended:
            if (!(move instanceof SurrenderEvent)) {
                throw GogomayaException.create(GogomayaError.GamePlayGameEnded);
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
            Collection<GameEvent<State>> events = processor.process(cache.getSession(), move);
            for (GameEvent<State> event : events)
                event.setSession(sessionId);
            // Step 6. Invoking appropriate notification
            notificationService.notify(cache.getPlayerIds(), events);
            // Step 7. Returning state of the game
            return cache.getSession().getState();
        } finally {
            reentrantLock.unlock();
        }
    }

}
