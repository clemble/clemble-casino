package com.clemble.casino.server.game.construction.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

import com.clemble.casino.game.lifecycle.initiation.event.GameInitiationCreatedEvent;
import com.clemble.casino.server.event.game.SystemGameInitiationDueEvent;
import com.clemble.casino.server.event.game.SystemGameStartedEvent;
import com.clemble.casino.server.event.schedule.SystemAddJobScheduleEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.ImmutablePair;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.lifecycle.initiation.GameInitiation;
import com.clemble.casino.game.lifecycle.initiation.event.GameInitiationCanceledEvent;
import com.clemble.casino.game.lifecycle.initiation.event.GameInitiationConfirmedEvent;
import com.clemble.casino.game.lifecycle.initiation.service.GameInitiationService;
import com.clemble.casino.server.ServerService;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;

public class ServerGameInitiationService implements GameInitiationService, ServerService {

    final static public long CANCEL_TIMEOUT_SECONDS = 10;
    final static private Logger LOG = LoggerFactory.getLogger(ServerGameInitiationService.class);

    // TODO This made static to test assumption that wrong beans wired in Spring context
    final static private ConcurrentHashMap<String, Entry<GameInitiation, Set<String>>> sessionToInitiation = new ConcurrentHashMap<String, Entry<GameInitiation, Set<String>>>();

    final private PendingGameInitiationService pendingInitiationService;
    final private ServerPlayerPresenceService presenceService;
    final private SystemNotificationService systemNotificationService;
    final private ServerNotificationService notificationService;

    public ServerGameInitiationService(
        PendingGameInitiationService pendingInitiationService,
        ServerPlayerPresenceService presenceService,
        ServerNotificationService notificationService,
        SystemNotificationService systemNotificationService) {
        this.presenceService = checkNotNull(presenceService);
        this.systemNotificationService = checkNotNull(systemNotificationService);
        this.pendingInitiationService = checkNotNull(pendingInitiationService);
        this.notificationService = checkNotNull(notificationService);
    }

    public void start(GameInitiation initiation) {
        LOG.debug("Creating {}", initiation);
        // Step 1. Sanity check
        if (initiation == null) {
            LOG.error("Invalid initiation {}", initiation);
            throw ClembleCasinoException.withKey(ClembleCasinoError.ServerError, initiation.getSessionKey());
        }
        if (sessionToInitiation.containsKey(initiation.getSessionKey())) {
            LOG.error("Already have {} pending", initiation.getSessionKey());
            throw ClembleCasinoException.withKey(ClembleCasinoError.ServerError, initiation.getSessionKey());
        }
        // Step 2. Adding to internal cache
        LOG.debug("Adding new pending session {}", initiation.getSessionKey());
        final String sessionKey = initiation.getSessionKey();
        sessionToInitiation.put(sessionKey, new ImmutablePair<GameInitiation, Set<String>>(initiation, new ConcurrentSkipListSet<String>()));
        // Step 3. Sending notification to the players, that they need to confirm
        LOG.debug("Notifying participants {}", initiation);
        notificationService.send(initiation.getParticipants(), new GameInitiationCreatedEvent(initiation));
        // Step 4. Scheduling Cancel task
        SystemAddJobScheduleEvent scheduleEvent = new SystemAddJobScheduleEvent(
            initiation.getSessionKey(),
            "initiation",
            new SystemGameInitiationDueEvent(initiation.getSessionKey()),
            new DateTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(CANCEL_TIMEOUT_SECONDS)));
        systemNotificationService.send(scheduleEvent);
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
            notificationService.send(initiation.getParticipants(), new GameInitiationCanceledEvent(sessionKey, initiation, confirmations));
            Collection<String> offlinePlayers = initiation.getParticipants();
            offlinePlayers.removeAll(confirmations);
            LOG.warn("Mark silent players as offline {}", offlinePlayers);
            for(String offlinePlayer: offlinePlayers) {
                presenceService.markOffline(offlinePlayer);
            }
        } else {
            LOG.info("Game already initiated, processing unchanged {}", sessionKey);
        }
    }

    @Override
    public GameInitiation confirm(String sessionKey){
        throw new IllegalAccessError();
    }

    public GameInitiation confirm(String player, String sessionKey) {
        // Step 1. Sanity check
        Entry<GameInitiation, Set<String>> initiationToConfirmed = sessionToInitiation.get(sessionKey);
        if (initiationToConfirmed == null) {
            LOG.error("Can't find {} as pending in ", sessionKey, sessionToInitiation);
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameInitiationInActive, player, sessionKey);
        }
        // Step 2. Adding confirmed player to initiation
        GameInitiation initiation = initiationToConfirmed.getKey();
        Set<String> confirmations = initiationToConfirmed.getValue();
        if (!initiation.isParticipates(player)) {
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameInitiationInvalidPlayer, player, sessionKey);
        }
        synchronized (confirmations) {
            confirmations.add(player);
            notificationService.send(initiation.getParticipants(), new GameInitiationConfirmedEvent(sessionKey, initiation, player));
            // Step 3. Checking everybody confirmed
            if (confirmations.size() == initiation.getParticipants().size()) {
                sessionToInitiation.remove(sessionKey);
                if (presenceService.areAvailable(initiation.getParticipants())) {
                    LOG.trace("{} all players available trigger game", sessionKey);
                    systemNotificationService.send(new SystemGameStartedEvent(sessionKey, initiation));
                } else {
                    // TODO remove session from the lists
                    LOG.trace("{} not all player available", sessionKey);
                }
            }
        }
        return initiation;
    }

    @Override
    public Collection<GameInitiation> getPending() {
        throw new IllegalAccessError();
    }

    public Collection<GameInitiation> getPending(String player) {
        return pendingInitiationService.getPending(player);
    }

    public GameInitiation get(String key) {
        return pendingInitiationService.get(key);
    }
}
