package com.clemble.casino.server.game.construction;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

import com.clemble.casino.game.construction.event.GameInitiationExpired;
import com.clemble.casino.server.executor.EventTaskExecutor;
import com.clemble.casino.server.game.action.GameInitiationExpirationTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.ImmutablePair;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.construction.GameInitiation;
import com.clemble.casino.game.event.GameInitiatedEvent;
import com.clemble.casino.game.event.GameInitiationCanceledEvent;
import com.clemble.casino.game.event.GameInitiationConfirmedEvent;
import com.clemble.casino.game.construction.service.GameInitiationService;
import com.clemble.casino.server.ServerService;
import com.clemble.casino.server.game.action.GameManagerFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;

public class ServerGameInitiationService implements GameInitiationService, ServerService {

    final static public long CANCEL_TIMEOUT_SECONDS = 10;
    final static private Logger LOG = LoggerFactory.getLogger(ServerGameInitiationService.class);

    // TODO This made static to test assumption that wrong beans wired in Spring context
    final static private ConcurrentHashMap<String, Entry<GameInitiation, Set<String>>> sessionToInitiation = new ConcurrentHashMap<>();

    final private GameManagerFactory managerFactory;
    final private PlayerNotificationService notificationService;
    final private ServerPlayerPresenceService presenceService;
    final private EventTaskExecutor taskExecutor;

    public ServerGameInitiationService(GameManagerFactory processor,
            ServerPlayerPresenceService presenceService,
            PlayerNotificationService notificationService,
            EventTaskExecutor taskExecutor) {
        this.presenceService = checkNotNull(presenceService);
        this.managerFactory = checkNotNull(processor);
        this.notificationService = checkNotNull(notificationService);
        this.taskExecutor = taskExecutor;
    }

    public void start(GameInitiation initiation) {
        LOG.debug("Creating {}", initiation);
        // Step 1. Sanity check
        if (initiation == null) {
            LOG.error("Invalid initiation {}", initiation);
            throw ClembleCasinoException.fromError(ClembleCasinoError.ServerError);
        }
        if (sessionToInitiation.containsKey(initiation.getSessionKey())) {
            LOG.error("Already have {} pending", initiation.getSessionKey());
            throw ClembleCasinoException.fromError(ClembleCasinoError.ServerError);
        }
        // Step 2. Adding to internal cache
        LOG.debug("Adding new pending session {}", initiation.getSessionKey());
        final String sessionKey = initiation.getSessionKey();
        sessionToInitiation.put(sessionKey, new ImmutablePair<GameInitiation, Set<String>>(initiation, new ConcurrentSkipListSet<String>()));
        // Step 3. Sending notification to the players, that they need to confirm
        LOG.debug("Notifying participants {}", initiation);
        notificationService.notify(initiation.getParticipants(), new GameInitiatedEvent(initiation));
        // Step 4. Scheduling Cancel task
        taskExecutor.schedule(new GameInitiationExpirationTask(sessionKey, new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(CANCEL_TIMEOUT_SECONDS))));
    }

    public void expire(String sessionKey) {
        // Step 1. Removing initiation from the list
        Entry<GameInitiation, Set<String>> initiationToConfirmed = sessionToInitiation.remove(sessionKey);
        LOG.debug("Initiation for {} is due {}", sessionKey, initiationToConfirmed);
        // Step 2. Sending notification event
        if (initiationToConfirmed != null) {
            LOG.warn("Canceling initiation {}", sessionKey);
            Set<String> confirmations = initiationToConfirmed.getValue();
            GameInitiation initiation = initiationToConfirmed.getKey();
            LOG.warn("Failed to initiate game {}", initiation);
            notificationService.notify(initiation.getParticipants(), new GameInitiationCanceledEvent(sessionKey, initiation, confirmations));
            Collection<String> offlinePlayers = initiation.getParticipants();
            offlinePlayers.removeAll(confirmations);
            LOG.warn("Mark silent players as offline {}", offlinePlayers);
            for(String offlinePlayer: offlinePlayers) {
                presenceService.markOffline(offlinePlayer);
            }
            LOG.warn("Notifying all participants of initiation cancel {}", sessionKey);
            notificationService.notify(initiation.getParticipants(), new GameInitiationExpired(sessionKey));
        } else {
            LOG.info("Game already initiated, processing unchanged {}", sessionKey);
        }
    }

    @Override
    public GameInitiation confirm(String sessionKey){
        throw new IllegalAccessError();
    }

    public GameInitiation confirm(String sessionKey, String player) {
        // Step 1. Sanity check
        Entry<GameInitiation, Set<String>> initiationToConfirmed = sessionToInitiation.get(sessionKey);
        if (initiationToConfirmed == null) {
            LOG.error("Can't find {} as pending in ", sessionKey, sessionToInitiation);
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameInitiationInActive);
        }
        // Step 2. Adding confirmed player to initiation
        GameInitiation initiation = initiationToConfirmed.getKey();
        Set<String> confirmations = initiationToConfirmed.getValue();
        if (!initiation.isParticipates(player)) {
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameInitiationInvalidPlayer);
        }
        synchronized (confirmations) {
            confirmations.add(player);
            notificationService.notify(initiation.getParticipants(), new GameInitiationConfirmedEvent(sessionKey, initiation, player));
            // Step 3. Checking everybody confirmed
            if (confirmations.size() == initiation.getParticipants().size()) {
                sessionToInitiation.remove(sessionKey);
                if (presenceService.markPlaying(initiation.getParticipants(), initiation.getSessionKey())) {
                    LOG.trace("{} successfully updated presences, starting a new game", sessionKey);
                    managerFactory.start(initiation, null);
                } else {
                    // TODO remove session from the lists
                    LOG.trace("{} failed to update presences", sessionKey);
                }
            }
        }
        return initiation;
    }

}
