package com.gogomaya.server.game.construct;

import java.util.Collection;

import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameInitiation;
import com.gogomaya.player.Presence;
import com.gogomaya.server.player.notification.PlayerNotificationListener;
import com.gogomaya.server.player.presence.PlayerPresenceServerService;

public class AvailabilityGameInitiatorManager implements GameInitiatorManager {

    final private PlayerPresenceServerService playerPresenceService;
    final private GameInitiatorService initiatorService;

    public AvailabilityGameInitiatorManager(final PlayerPresenceServerService playerPresenceService, final GameInitiatorService initiatorService) {
        this.playerPresenceService = playerPresenceService;
        this.initiatorService = initiatorService;
    }

    public void register(final GameConstruction availabilityRequest) {
        // Step 1. Checking all users are active
        final Collection<Long> participants = availabilityRequest.fetchAcceptedParticipants();
        // Step 2. Checking all participants are available
        final GameInitiation initiation = new GameInitiation(availabilityRequest.getRequest().getSpecification().getName().getGame(), availabilityRequest.getSession(), participants, availabilityRequest.getRequest()
                .getSpecification());
        if (!initiatorService.initiate(initiation)) {
            // Step 2.1 Pretty naive implementation of MessageListener functionality
            playerPresenceService.subscribe(participants, new PlayerNotificationListener<Presence>() {

                @Override
                public void onUpdate(long playerId, Presence state) {
                    if (initiatorService.initiate(initiation))
                        playerPresenceService.unsubscribe(participants, this);
                }
            });
        }
    }
}
