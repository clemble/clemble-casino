package com.clemble.casino.server.connection.listener;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.player.event.PlayerDiscoveredConnectionEvent;
import com.clemble.casino.server.event.player.SystemPlayerDiscoveredConnectionEvent;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.SystemEventListener;

public class PlayerDiscoveryNotificationEventListener implements SystemEventListener<SystemPlayerDiscoveredConnectionEvent>{

    final PlayerNotificationService notificationService;

    public PlayerDiscoveryNotificationEventListener(PlayerNotificationService notificationService) {
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public void onEvent(SystemPlayerDiscoveredConnectionEvent event) {
        // Step 1. Notifying original
        notificationService.send(new PlayerDiscoveredConnectionEvent(event.getPlayer(), event.getDiscovered()));
        // Step 1. Notifying discovered player
        notificationService.send(new PlayerDiscoveredConnectionEvent(event.getDiscovered(), event.getPlayer()));
    }

    @Override
    public String getChannel() {
        return SystemPlayerDiscoveredConnectionEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerDiscoveredConnectionEvent.CHANNEL + " > player:social:autodiscovery";
    }

}
