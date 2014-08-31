package com.clemble.casino.server.game.construction;

import com.clemble.casino.game.construct.ServerGameInitiation;
import com.clemble.casino.server.event.game.SystemGameEndedEvent;
import com.clemble.casino.server.event.game.SystemGameReadyEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;

/**
 * Created by mavarazy on 8/31/14.
 */
public class ServerGameReadyEventListener implements SystemEventListener<SystemGameReadyEvent>{

    final private ServerGameInitiationService initiationService;

    public ServerGameReadyEventListener(ServerGameInitiationService initiationService) {
        this.initiationService = initiationService;
    }

    @Override
    public void onEvent(SystemGameReadyEvent event) {
        initiationService.start(event.getInitiation());
    }

    @Override
    public String getChannel() {
        return SystemGameReadyEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemGameReadyEvent.CHANNEL + " > game:management";
    }
}
