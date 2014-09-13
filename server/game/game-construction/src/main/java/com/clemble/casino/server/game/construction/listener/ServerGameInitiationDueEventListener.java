package com.clemble.casino.server.game.construction.listener;

import com.clemble.casino.server.event.game.SystemGameInitiationDueEvent;
import com.clemble.casino.server.game.construction.service.ServerGameInitiationService;
import com.clemble.casino.server.player.notification.SystemEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mavarazy on 9/12/14.
 */
public class ServerGameInitiationDueEventListener implements SystemEventListener<SystemGameInitiationDueEvent> {

    final static private Logger LOG = LoggerFactory.getLogger(ServerGameInitiationDueEventListener.class);

    final private ServerGameInitiationService initiationService;

    public ServerGameInitiationDueEventListener(ServerGameInitiationService initiationService) {
        this.initiationService = initiationService;
    }

    @Override
    public void onEvent(SystemGameInitiationDueEvent event) {
        LOG.debug("Processing started {}", event.getSessionKey());
        initiationService.expire(event.getSessionKey());
        LOG.debug("Processing finished {}", event.getSessionKey());
    }

    @Override
    public String getChannel() {
        return SystemGameInitiationDueEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemGameInitiationDueEvent.CHANNEL + " > game:construction";
    }
}
