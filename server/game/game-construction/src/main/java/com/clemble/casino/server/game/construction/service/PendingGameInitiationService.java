package com.clemble.casino.server.game.construction.service;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.*;

import com.clemble.casino.lifecycle.initiation.InitiationState;
import com.clemble.casino.game.construction.GameInitiation;
import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.event.game.SystemGameReadyEvent;
import com.clemble.casino.server.game.construction.PendingGameInitiation;
import com.clemble.casino.server.game.construction.PendingPlayer;
import com.clemble.casino.server.game.construction.repository.PendingGameInitiationRepository;
import com.clemble.casino.server.game.construction.repository.PendingPlayerRepository;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;

public class PendingGameInitiationService {

    final private ServerPlayerPresenceService presenceService;
    final private SystemNotificationService notificationService;

    final private PendingPlayerRepository playerRepository;
    final private PendingGameInitiationRepository initiationRepository;

    public PendingGameInitiationService(
        PendingPlayerRepository playerRepository,
        PendingGameInitiationRepository initiationRepository,
        ServerPlayerPresenceService presenceService,
        SystemNotificationService notificationService) {
        this.playerRepository = checkNotNull(playerRepository);
        this.presenceService = presenceService;
        this.notificationService = notificationService;
        this.initiationRepository = checkNotNull(initiationRepository);
    }

   public void add(final GameInitiation initiation) {
        final Collection<String> players = initiation.getParticipants();
        if (!presenceService.areAvailable(players)) {
            Set<PendingPlayer> pendingPlayers = new HashSet<>();
            for (String player : players) {
                PendingPlayer pending = playerRepository.findByPropertyValue("player", player);
                if(pending != null)
                    pendingPlayers.add(pending);
            }
            // Step 1. Saving new PendingGameInitiation
            initiationRepository.save(new PendingGameInitiation(initiation, pendingPlayers));
        } else {
            notificationService.notify(new SystemGameReadyEvent(initiation));
        }
    }

    public Collection<GameInitiation> getPending(String player) {
        // Step 1. Fetching data from pending player
        Collection<GameInitiation> initiations = new ArrayList<>();
        for (PendingGameInitiation initiation : playerRepository.findPending(player))
            initiations.add(toInitiation(initiation));
        // Step 2. Returning pending initiations
        return initiations;
    }

    public GameInitiation toInitiation(PendingGameInitiation initiation) {
        GameConfiguration configuration = initiation.getConfiguration();
        return new GameInitiation(initiation.getSessionKey(), InitiationState.pending, PlayerAwareUtils.toPlayerList(initiation.getParticipants()), configuration);
    }
}
