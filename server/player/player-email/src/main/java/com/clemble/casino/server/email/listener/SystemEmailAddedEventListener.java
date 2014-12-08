package com.clemble.casino.server.email.listener;

import com.clemble.casino.server.email.PlayerEmail;
import com.clemble.casino.server.email.repository.PlayerEmailRepository;
import com.clemble.casino.server.email.service.ServerPlayerEmailService;
import com.clemble.casino.server.event.email.SystemEmailAddedEvent;
import com.clemble.casino.server.event.email.SystemEmailVerifiedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.notification.SystemNotificationService;

/**
 * Created by mavarazy on 12/6/14.
 */
public class SystemEmailAddedEventListener implements SystemEventListener<SystemEmailAddedEvent> {

    final private ServerPlayerEmailService serverPlayerEmailService;

    public SystemEmailAddedEventListener(ServerPlayerEmailService serverPlayerEmailService) {
        this.serverPlayerEmailService = serverPlayerEmailService;
    }

    @Override
    public void onEvent(SystemEmailAddedEvent event) {
        // Step 1. Appending player email
        serverPlayerEmailService.add(event.getPlayer(), event.getEmail(), event.getVerified());
    }

    @Override
    public String getChannel() {
        return SystemEmailAddedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemEmailAddedEvent.CHANNEL + " > player:email";
    }
}
