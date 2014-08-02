package com.clemble.casino.server.presence.listener;

import com.clemble.casino.server.event.player.SystemPlayerLeftEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;

/**
 * Created by mavarazy on 7/4/14.
 */
public class PlayerPresenceLeftEventListener implements SystemEventListener<SystemPlayerLeftEvent> {

    final private ServerPlayerPresenceService presenceService;

    public PlayerPresenceLeftEventListener(ServerPlayerPresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @Override
    public void onEvent(SystemPlayerLeftEvent event) {
        presenceService.markOffline(event.getPlayer());
    }

    @Override
    public String getChannel() {
        return SystemPlayerLeftEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerLeftEvent.CHANNEL + " > presence:left";
    }
}
