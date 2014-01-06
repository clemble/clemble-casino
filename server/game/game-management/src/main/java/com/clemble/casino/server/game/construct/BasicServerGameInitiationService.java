package com.clemble.casino.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameInitiatedEvent;
import com.clemble.casino.game.event.server.GameInitiationCanceledEvent;
import com.clemble.casino.game.event.server.GameInitiationConfirmedEvent;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.game.PendingGameInitiation;
import com.clemble.casino.server.game.PendingPlayer;
import com.clemble.casino.server.game.action.GameSessionProcessor;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.repository.game.PendingGameInitiationRepository;
import com.clemble.casino.server.repository.game.PendingPlayerRepository;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class BasicServerGameInitiationService implements ServerGameInitiationService {

    final static public long CANCEL_TIMEOUT_SECONDS = 10;
    final private Logger LOG = LoggerFactory.getLogger(BasicServerGameInitiationService.class);

    final private Map<GameSessionKey, GameInitiation> sessionToInitiation = new ConcurrentHashMap<>();

    final private ServerPlayerPresenceService presenceService;
    final private PlayerNotificationService notificationService;
    final private GameSessionProcessor<?> processor;
    final private PendingPlayerRepository playerRepository;
    final private PendingGameInitiationRepository initiationRepository;

    final private ScheduledExecutorService executorService;

    public BasicServerGameInitiationService(
            GameSessionProcessor<?> processor,
            ServerPlayerPresenceService presenceService,
            PlayerNotificationService notificationService,
            PendingPlayerRepository playerRepository,
            PendingGameInitiationRepository initiationRepository,
            ScheduledExecutorService executorService) {
        this.processor = checkNotNull(processor);

        this.presenceService = checkNotNull(presenceService);
        this.initiationRepository = checkNotNull(initiationRepository);
        this.playerRepository = checkNotNull(playerRepository);
        this.notificationService = checkNotNull(notificationService);

        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder().setNameFormat("Game initiation - %d");
        this.executorService = Executors.newScheduledThreadPool(5, threadFactoryBuilder.build());
    }

    @Override
    public void register(final GameInitiation initiation) {
        final Collection<String> players = PlayerAwareUtils.toPlayerList(initiation.getParticipants());
        if (!presenceService.areAvailable(players)) {
            // TODO make automatic creation part of initialization flow
            Collection<PendingPlayer> pendingPlayers = new ArrayList<>();
            for(String player: players)
                pendingPlayers.add(new PendingPlayer(player));
            playerRepository.save(pendingPlayers);
            // Step 1. Saving new PendingGameInitiation
            initiationRepository.save(new PendingGameInitiation(initiation));
        } else {
            start(initiation);
        }
    }

    @Override
    public void start(final GameInitiation initiation) {
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
                GameInitiation initiation = sessionToInitiation.remove(sessionKey);
                // Step 2. Sending notification event
                if (initiation != null) {
                    notificationService.notifyAll(initiation.getParticipants(), new GameInitiationCanceledEvent(sessionKey, initiation));
                    Collection<String> offlinePlayers = PlayerAwareUtils.toPlayerList(initiation.getParticipants());
                    offlinePlayers.removeAll(initiation.getConfirmations());
                    presenceService.markOffline(offlinePlayers);
                }
            }
        }, CANCEL_TIMEOUT_SECONDS, TimeUnit.SECONDS);
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

    @Override
    public Collection<GameInitiation> pending(Game game, String player) {
        // TODO add filtering by game
        // Step 1. Fetching data from pending player
        Collection<GameInitiation> initiations = new ArrayList<>();
        for(PendingGameInitiation initiation: playerRepository.findPending(player))
            initiations.add(initiation.toInitiation());
        // Step 2. Returning pending initiations
        return initiations;
    }

}
