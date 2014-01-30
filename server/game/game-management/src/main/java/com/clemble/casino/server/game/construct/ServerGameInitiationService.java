package com.clemble.casino.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.ImmutablePair;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameInitiatedEvent;
import com.clemble.casino.game.event.server.GameInitiationCanceledEvent;
import com.clemble.casino.game.event.server.GameInitiationConfirmedEvent;
import com.clemble.casino.game.service.GameInitiationService;
import com.clemble.casino.server.ServerService;
import com.clemble.casino.server.game.action.GameSessionProcessor;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class ServerGameInitiationService implements GameInitiationService, ServerService {

    final static public long CANCEL_TIMEOUT_SECONDS = 10;
    final private Logger LOG = LoggerFactory.getLogger(ServerGameInitiationService.class);

    final private ConcurrentHashMap<GameSessionKey, Entry<GameInitiation, Set<String>>> sessionToInitiation = new ConcurrentHashMap<>();

    final private GameSessionProcessor<?> processor;
    final private PlayerNotificationService notificationService;
    final private ServerPlayerPresenceService presenceService;

    final private ScheduledExecutorService executorService;

    public ServerGameInitiationService(GameSessionProcessor<?> processor,
            ServerPlayerPresenceService presenceService,
            PlayerNotificationService notificationService) {
        this.presenceService = checkNotNull(presenceService);
        this.processor = checkNotNull(processor);
        this.notificationService = checkNotNull(notificationService);

        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder().setNameFormat("CL game.initiation %d");
        this.executorService = Executors.newScheduledThreadPool(5, threadFactoryBuilder.build());
    }

    public void start(GameInitiation initiation) {
        // Step 1. Sanity check
        if (initiation == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.ServerError);
        if (sessionToInitiation.containsKey(initiation.getSession()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.ServerError);
        // Step 2. Adding to internal cache
        final GameSessionKey sessionKey = initiation.getSession();
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                // Step 1. Removing initiation from the list
                Entry<GameInitiation, Set<String>> initiationToConfirmed = sessionToInitiation.remove(sessionKey);
                // Step 2. Sending notification event
                if (initiationToConfirmed != null) {
                    Set<String> confirmations = initiationToConfirmed.getValue();
                    GameInitiation initiation = initiationToConfirmed.getKey();
                    notificationService.notify(initiation.getParticipants(), new GameInitiationCanceledEvent(sessionKey, initiation, confirmations));
                    Collection<String> offlinePlayers = initiation.getParticipants();
                    offlinePlayers.removeAll(confirmations);
                    presenceService.markOffline(offlinePlayers);
                }
            }
        }, CANCEL_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        sessionToInitiation.put(initiation.getSession(), new ImmutablePair<GameInitiation, Set<String>>(initiation, new HashSet<String>()));
        // Step 3. Sending notification to the players, that they need to confirm
        notificationService.notify(initiation.getParticipants(), new GameInitiatedEvent(initiation));
    }

    @Override
    public GameInitiation confirm(Game game, String session, String player) {
        GameSessionKey sessionKey = new GameSessionKey(game, session);
        // Step 1. Sanity check
        Entry<GameInitiation, Set<String>> initiationToConfirmed = sessionToInitiation.get(sessionKey);
        if (initiationToConfirmed == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameInitiationInActive);
        // Step 2. Adding confirmed player to initiation
        GameInitiation initiation = initiationToConfirmed.getKey();
        Set<String> confirmations = initiationToConfirmed.getValue();
        if (!initiation.isParticipates(player)) {
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameInitiationInvalidPlayer);
        } else {
            confirmations.add(player);
        }
        // Step 3. Checking everybody confirmed
        if (confirmations.size() == initiation.getParticipants().size()) {
            sessionToInitiation.remove(sessionKey);
            if (presenceService.markPlaying(initiation.getParticipants(), initiation.getSession())) {
                LOG.trace("Successfully updated presences, starting a new game");
                processor.start(initiation);
            } else {
                // TODO remove session from the lists
                LOG.trace("Failed to update presences");
            }
        } else {
            notificationService.notify(initiation.getParticipants(), new GameInitiationConfirmedEvent(sessionKey, initiation, player));
        }
        return initiation;
    }

}
