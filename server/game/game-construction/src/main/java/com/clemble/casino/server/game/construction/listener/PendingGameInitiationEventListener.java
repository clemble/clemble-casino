package com.clemble.casino.server.game.construction.listener;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.*;

import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.player.Presence;
import com.clemble.casino.server.event.game.SystemGameReadyEvent;
import com.clemble.casino.server.event.player.SystemPlayerPresenceChangedEvent;
import com.clemble.casino.server.game.construction.PendingGameInitiation;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.game.construction.repository.PendingGameInitiationRepository;
import com.clemble.casino.server.game.construction.repository.PendingPlayerRepository;

public class PendingGameInitiationEventListener implements SystemEventListener<SystemPlayerPresenceChangedEvent> {

    final private ServerPlayerPresenceService presenceService;
    final private SystemNotificationService notificationService;

    final private PendingPlayerRepository playerRepository;
    final private PendingGameInitiationRepository initiationRepository;

    public PendingGameInitiationEventListener(PendingPlayerRepository playerRepository,
            PendingGameInitiationRepository initiationRepository,
            ServerPlayerPresenceService presenceService,
            SystemNotificationService notificationService) {
        this.notificationService = checkNotNull(notificationService);
        this.presenceService = checkNotNull(presenceService);
        this.initiationRepository = checkNotNull(initiationRepository);
        this.playerRepository = checkNotNull(playerRepository);
    }

    @Override
    public void onEvent(SystemPlayerPresenceChangedEvent event) {
        // Step 1. Checking only if presence is online
        if (event.getPresence() == Presence.online) {
            // Step 2. Fetching pending initiations
            List<PendingGameInitiation> pendingGameInitiations = playerRepository.findPending(event.getPlayer());
            for (PendingGameInitiation initiation : pendingGameInitiations) {
                // Step 3. Checking we can start game
                List<String> players = PlayerAwareUtils.toPlayerList(initiation.getParticipants());
                if (presenceService.areAvailable(players)) {
                    notificationService.send(new SystemGameReadyEvent(initiation.toInitiation()));
                    initiationRepository.delete(initiation.getId());
                    break;
                }
            }
        }
    }

    @Override
    public String getChannel() {
        return SystemPlayerPresenceChangedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerPresenceChangedEvent.CHANNEL + " > game:construction";
    }

}
