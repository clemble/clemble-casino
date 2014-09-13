package com.clemble.casino.server.game.listener;

import com.clemble.casino.game.construction.event.GameInitiationCanceledEvent;
import com.clemble.casino.server.event.game.SystemGameStartedEvent;
import com.clemble.casino.server.game.action.GameManagerFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mavarazy on 9/13/14.
 */
public class ServerGameStartedEventListener implements SystemEventListener<SystemGameStartedEvent> {

    final private Logger LOG = LoggerFactory.getLogger(ServerGameStartedEventListener.class);

    final private GameManagerFactory managerFactory;
    final private ServerPlayerPresenceService presenceService;
    final private PlayerNotificationService notificationService;

    public ServerGameStartedEventListener(GameManagerFactory managerFactory, ServerPlayerPresenceService presenceService, PlayerNotificationService notificationService) {
        this.managerFactory = managerFactory;
        this.presenceService = presenceService;
        this.notificationService = notificationService;
    }

    @Override
    public void onEvent(SystemGameStartedEvent event) {
        LOG.debug("{} started event received", event.getSessionKey());
        if(presenceService.markPlaying(event.getInitiation().getParticipants(), event.getInitiation().getSessionKey())) {
            LOG.debug("{} able to mark all players playing", event.getSessionKey());
            // Case 1. Starting game in managerFactory
            managerFactory.start(event.getInitiation(), null);
        } else {
            LOG.debug("{} failed to mark all players as playing", event.getSessionKey());
            // Case 2. Canceling game initiation
            notificationService.notify(event.getInitiation().getParticipants(), new GameInitiationCanceledEvent(event.getSessionKey(), event.getInitiation(), event.getInitiation().getParticipants()));
        }
    }

    @Override
    public String getChannel() {
        return SystemGameStartedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemGameStartedEvent.CHANNEL + " > game:management";
    }
}
