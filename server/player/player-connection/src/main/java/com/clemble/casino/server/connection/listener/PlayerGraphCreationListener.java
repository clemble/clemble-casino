package com.clemble.casino.server.connection.listener;

import com.clemble.casino.server.connection.service.PlayerGraphService;
import com.clemble.casino.server.event.player.SystemPlayerCreatedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;

/**
 * Created by mavarazy on 7/4/14.
 */
public class PlayerGraphCreationListener implements SystemEventListener<SystemPlayerCreatedEvent>{

    final private PlayerGraphService graphService;

    public PlayerGraphCreationListener(PlayerGraphService socialNetworkRepository) {
        this.graphService = socialNetworkRepository;
    }

    @Override
    public void onEvent(SystemPlayerCreatedEvent event) {
        // Step 0. Check Connection was not yet created
        if (graphService.getConnections(event.getPlayer()) != null)
            return;
        // Step 1. Saving connections
        graphService.create(event.getPlayer());
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
