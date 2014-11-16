package com.clemble.casino.server.aspect.notification;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collection;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.event.ManagementEvent;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

public class PrivateNotificationRuleAspect extends ClembleAspect<ManagementEvent> {

    final private Collection<String> participants;
    final private PlayerNotificationService notificationService;

    public PrivateNotificationRuleAspect(Collection<String> participants, PlayerNotificationService notificationService) {
        super(new EventTypeSelector(ManagementEvent.class));
        this.participants = checkNotNull(participants);
        this.notificationService = checkNotNull(notificationService);
    }


    @Override
    protected void doEvent(ManagementEvent event) {
        // Step 1. Sending only to participants
        notificationService.send(participants, event);
    }

}
