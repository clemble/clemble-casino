package com.clemble.casino.server.connection.listener;

import com.clemble.casino.server.connection.service.PlayerGraphService;
import com.clemble.casino.server.event.player.SystemPlayerCreatedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;

/**
 * Created by mavarazy on 7/4/14.
 */
public class PlayerConnectionNetworkCreationListener implements SystemEventListener<SystemPlayerCreatedEvent>{

    final private PlayerGraphService socialNetworkRepository;

    public PlayerConnectionNetworkCreationListener(PlayerGraphService socialNetworkRepository) {
        this.socialNetworkRepository = socialNetworkRepository;
    }

    @Override
    public void onEvent(SystemPlayerCreatedEvent event) {
        // Step 0. Check Connection was not yet created
        if (socialNetworkRepository.getConnections(event.getPlayer()) != null)
            return;
        // Step 1. Saving connections
        socialNetworkRepository.create(event.getPlayer());
    }

    @Override
    public String getChannel() {
        return SystemPlayerCreatedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerCreatedEvent.CHANNEL + " > player:connection";
    }
}
