package com.clemble.casino.server.game.construct;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.List;

import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.player.Presence;
import com.clemble.casino.server.event.SystemPlayerPresenceChangedEvent;
import com.clemble.casino.server.game.PendingGameInitiation;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.repository.game.PendingGameInitiationRepository;
import com.clemble.casino.server.repository.game.PendingPlayerRepository;

public class PendingGameInitiationListener implements SystemEventListener<SystemPlayerPresenceChangedEvent> {

    final private PendingPlayerRepository playerRepository;
    final private PendingGameInitiationRepository initiationRepository;
    final private ServerPlayerPresenceService presenceService;
    final private ServerGameInitiationService initiationService;

    public PendingGameInitiationListener(
            PendingPlayerRepository playerRepository,
            PendingGameInitiationRepository initiationRepository,
            ServerPlayerPresenceService presenceService,
            ServerGameInitiationService initiationService) {
        this.initiationService = checkNotNull(initiationService);
        this.presenceService = checkNotNull(presenceService);
        this.initiationRepository = checkNotNull(initiationRepository);
        this.playerRepository = checkNotNull(playerRepository);
    }

    @Override
    public void onEvent(SystemPlayerPresenceChangedEvent event) {
        // Step 1. Checking only if presence is online
        if (event.getPresence() == Presence.online) {
            // Step 2. Fetching pending initiations
            for (PendingGameInitiation initiation : playerRepository.findPending(event.getPlayer())) {
                // Step 3. Checking we can start game
                List<String> players = PlayerAwareUtils.toPlayerList(initiation.getParticipants());
                if (presenceService.areAvailable(players)) {
                    initiationService.start(initiation.toInitiation());
                    initiationRepository.delete(initiation.getId());
                    break;
                }
            }
        }
    }

    @Override
    public String getChannel(){
        return SystemPlayerPresenceChangedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return "game.pending";
    }

}
