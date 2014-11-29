package com.clemble.casino.server.game.aspect.notification;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collection;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.player.notification.ServerNotificationService;

public class GamePrivateNotificationRuleAspect extends GameAspect<GameManagementEvent> {

    final private Collection<String> participants;
    final private ServerNotificationService notificationService;

    public GamePrivateNotificationRuleAspect(Collection<String> participants, ServerNotificationService notificationService) {
        super(new EventTypeSelector(GameManagementEvent.class));
        this.participants = checkNotNull(participants);
        this.notificationService = checkNotNull(notificationService);
    }


    @Override
    protected void doEvent(GameManagementEvent event) {
        // Step 1. Sending only to participants
        notificationService.send(participants, event);
    }

}
