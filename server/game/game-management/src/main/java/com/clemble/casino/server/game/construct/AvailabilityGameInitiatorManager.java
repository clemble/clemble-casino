package com.clemble.casino.server.game.construct;

import java.util.Collection;

import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.player.Presence;
import com.clemble.casino.server.player.notification.PlayerNotificationListener;
import com.clemble.casino.server.player.presence.PlayerPresenceListenerService;

public class AvailabilityGameInitiatorManager implements GameInitiatorManager {

    final private PlayerPresenceListenerService playerPresenceService;
    final private GameInitiatorService initiatorService;

    public AvailabilityGameInitiatorManager(final PlayerPresenceListenerService playerPresenceService, final GameInitiatorService initiatorService) {
        this.playerPresenceService = playerPresenceService;
        this.initiatorService = initiatorService;
    }

    public void register(final GameConstruction availabilityRequest) {
        // Step 1. Checking all users are active
        final Collection<String> participants = availabilityRequest.fetchAcceptedParticipants();
        // Step 2. Checking all participants are available
        final GameInitiation initiation = new GameInitiation(availabilityRequest.getSession(), participants, availabilityRequest.getRequest().getSpecification());
        if (!initiatorService.initiate(initiation)) {
            // Step 2.1 Pretty naive implementation of MessageListener functionality
            playerPresenceService.subscribe(participants, new PlayerNotificationListener<Presence>() {

                @Override
                public void onUpdate(String player, Presence state) {
                    if (initiatorService.initiate(initiation))
                        playerPresenceService.unsubscribe(participants, this);
                }

                @Override
                public String toString(){
                    return initiation.getSession().toString();
                }
            });
        }
    }
}
