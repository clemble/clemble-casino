package com.clemble.casino.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.locks.ReentrantLock;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameSessionState;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.MatchGameRecord;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.event.server.GameStartedEvent;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.server.game.cache.GameCache;
import com.clemble.casino.server.game.cache.GameCacheService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.repository.game.GameSessionRepository;

public class MatchGameManager<S extends GameState> implements GameManager<MatchGameRecord<S>> {

    final private GameCacheService<S> cacheService;
    final private GameStateFactory<S> stateFactory;
    final private GameSessionRepository<S> sessionRepository;
    final private PlayerNotificationService notificationService;

    public MatchGameManager(
            final GameStateFactory<S> stateFactory,
            final GameSessionRepository<S> sessionRepository,
            final GameCacheService<S> cacheService,
            final PlayerNotificationService notificationService) {
        this.stateFactory = checkNotNull(stateFactory);
        this.cacheService = checkNotNull(cacheService);
        this.sessionRepository = checkNotNull(sessionRepository);
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public MatchGameRecord<S> start(GameInitiation initiation) {
        // Step 1. Allocating table for game initiation
        S state = stateFactory.constructState(initiation, new GameContext(initiation, (MatchGameConfiguration) initiation.getConfiguration()));
        MatchGameRecord<S> matchRecord = new MatchGameRecord<S>()
            .setSession(initiation.getSession())
            .setConfiguration(initiation.getConfiguration().getConfigurationKey())
            .setSessionState(GameSessionState.active)
            .setPlayers(initiation.getParticipants())
            .setState(state);
        matchRecord = sessionRepository.saveAndFlush(matchRecord);
        // Step 2. Sending notification for game started
        notificationService.notify(initiation.getParticipants(), new GameStartedEvent<S>(matchRecord));
        // Step 3. Returning active table
        return matchRecord;
    }

    @Override
    public GameManagementEvent process(GameSessionKey sessionKey, GameAction action) {
        // Step 1. Sanity check
        if (action == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayMoveUndefined);
        // Step 2. Acquiring lock for session event processing
        GameCache<S> cache = cacheService.get(sessionKey);
        // Step 3. Checking
        switch (cache.getSession().getSessionState()) {
        case finished:
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayGameEnded);
        default:
            break;
        }
        ReentrantLock reentrantLock = cache.getSessionLock();
        // Step 3. Acquiring lock for the session, to exclude parallel processing
        reentrantLock.lock();
        try {
            // Step 4. Retrieving game processor based on session identifier
            GameProcessor<S> processor = cache.getProcessor();
            // Step 5. Processing movement
            return processor.process(cache.getSession(), action);
        } finally {
            reentrantLock.unlock();
        }
    }

}
