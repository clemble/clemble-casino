package com.clemble.casino.server.social;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.player.PlayerConnectionDiscoveredEvent;
import com.clemble.casino.server.event.SystemPlayerConnectionDiscoveredEvent;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.SystemEventListener;

public class SocialPlayerConnectionDiscoveredNotifier implements SystemEventListener<SystemPlayerConnectionDiscoveredEvent>{

    final PlayerNotificationService notificationService;

    public SocialPlayerConnectionDiscoveredNotifier(PlayerNotificationService notificationService) {
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public void onEvent(SystemPlayerConnectionDiscoveredEvent event) {
        // Step 1. Notifying original
        notificationService.notify(new PlayerConnectionDiscoveredEvent(event.getPlayer(), event.getDiscovered()));
        // Step 1. Notifying discovered player
        notificationService.notify(new PlayerConnectionDiscoveredEvent(event.getDiscovered(), event.getPlayer()));
    }

    @Override
    public String getChannel() {
        return SystemPlayerConnectionDiscoveredEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return "player.social.autodiscovery";
    }

}
