package com.clemble.casino.server.presence.listener;

import com.clemble.casino.server.event.game.SystemGameEndedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;

/**
 * Created by mavarazy on 7/4/14.
 */
public class PlayerPresenceGameStartedEventListener implements SystemEventListener<SystemGameEndedEvent> {

    final private ServerPlayerPresenceService presenceService;

    public PlayerPresenceGameStartedEventListener(ServerPlayerPresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @Override
    public void onEvent(SystemGameEndedEvent event) {
        // TODO make check that player is currently occupied by the game or free
        presenceService.markPlaying(event.getParticipants(), event.getSessionKey());
    }

    @Override
    public String getChannel() {
        return SystemGameEndedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemGameEndedEvent.CHANNEL + " > presence:game:started";
    }

}
