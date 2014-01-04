package com.clemble.casino.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameInitiatedEvent;
import com.clemble.casino.game.event.server.GameInitiationConfirmedEvent;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.event.SystemPlayerPresenceChangedEvent;
import com.clemble.casino.server.game.action.GameSessionProcessor;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.player.presence.SystemNotificationServiceListener;

public class BasicServerGameInitiationService implements ServerGameInitiationService {

    final private Logger LOG = LoggerFactory.getLogger(BasicServerGameInitiationService.class);

    final private Map<GameSessionKey, GameInitiation> sessionToInitiation = new ConcurrentHashMap<>();

    final private ServerPlayerPresenceService presenceService;
    final private PlayerNotificationService notificationService;
    final private GameSessionProcessor<?> processor;

    final private SystemNotificationServiceListener systemListener;

    public BasicServerGameInitiationService(
            GameSessionProcessor<?> processor,
            ServerPlayerPresenceService presenceService,
            PlayerNotificationService notificationService,
            SystemNotificationServiceListener systemListener) {
        this.processor = checkNotNull(processor);

        this.systemListener = checkNotNull(systemListener);
        this.presenceService = checkNotNull(presenceService);
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public void register(final GameInitiation initiation) {
        final Collection<String> players = PlayerAwareUtils.toPlayerList(initiation.getParticipants());
        if (!presenceService.areAvailable(players)) {
            systemListener.subscribe(players, new SystemEventListener<SystemEvent>() {

                @Override
                public void onEvent(String player, SystemEvent event) {
                    if (event instanceof SystemPlayerPresenceChangedEvent) {
                        if (presenceService.areAvailable(players)) {
                            systemListener.unsubscribe(players, this);
                            start(initiation);
                        }
                    }
                }

                @Override
                public String toString() {
                    return "systemListener:" + initiation.getSession();
                }
            });
        } else {
            start(initiation);
        }
    }

    @Override
    public void start(GameInitiation initiation) {
        // Step 1. Sanity check
        if (initiation == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.ServerError);
        if (sessionToInitiation.containsKey(initiation.getSession()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.ServerError);
        // Step 2. Adding to internal cache
        sessionToInitiation.put(initiation.getSession(), initiation);
        // Step 3. Sending notification to the players, that they need to confirm
        notificationService.notifyAll(initiation.getParticipants(), new GameInitiatedEvent(initiation));
    }

    @Override
    public GameInitiation ready(GameSessionKey sessionKey, String player) {
        // Step 1. Sanity check
        if (sessionToInitiation.get(sessionKey) == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameInitiationInActive);
        // Step 2. Adding confirmed player to initiation
        GameInitiation initiation = sessionToInitiation.get(sessionKey);
        initiation.addConfirmation(player);
        if (initiation.confirmed()) {
            GameInitiation readyInitiation = sessionToInitiation.remove(sessionKey);
            if (readyInitiation == null)
                return initiation;
            if (presenceService.markPlaying(PlayerAwareUtils.toPlayerList(initiation.getParticipants()), initiation.getSession())) {
                LOG.trace("Successfully updated presences, starting a new game");
                processor.start(initiation);
            } else {
                LOG.trace("Failed to update presences");
            }
        } else {
            notificationService.notifyAll(initiation.getParticipants(), new GameInitiationConfirmedEvent(sessionKey, initiation, player));
        }
        return initiation;
    }

}
