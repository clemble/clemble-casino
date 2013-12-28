package com.clemble.casino.server.game.construct;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.event.PlayerPresenceChangedEvent;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.presence.SystemNotificationServiceListener;

public class AvailabilityGameInitiatorManager implements GameInitiatorManager {

    final private Logger LOG = LoggerFactory.getLogger(AvailabilityGameInitiatorManager.class);

    final private SystemNotificationServiceListener playerPresenceService;
    final private GameInitiatorService initiatorService;

    public AvailabilityGameInitiatorManager(final SystemNotificationServiceListener playerPresenceService, final GameInitiatorService initiatorService) {
        this.playerPresenceService = playerPresenceService;
        this.initiatorService = initiatorService;
    }

    public void register(final GameConstruction request) {
        LOG.trace("Registering {}", request);
        // Step 1. Checking all users are active
        final Collection<String> participants = request.fetchAcceptedParticipants();
        LOG.trace("Participants {}", participants);
        // Step 2. Checking all participants are available
        final GameInitiation initiation = new GameInitiation(request);
        LOG.debug("Constructed initiation {}", initiation);
        if (!initiatorService.initiate(initiation)) {
            // Step 2.1 Pretty naive implementation of MessageListener functionality
            playerPresenceService.subscribe(participants, new SystemEventListener<SystemEvent>() {

                @Override
                public void onEvent(String player, SystemEvent event) {
                    if (event instanceof PlayerPresenceChangedEvent) {
                        if (initiatorService.initiate(initiation))
                            playerPresenceService.unsubscribe(participants, this);
                    }
                }

                @Override
                public String toString() {
                    return initiation.getSession().toString();
                }
            });
        }
    }
}
