package com.clemble.casino.server.email.listener;

import com.clemble.casino.server.email.PlayerEmail;
import com.clemble.casino.server.email.repository.PlayerEmailRepository;
import com.clemble.casino.server.email.service.PlayerEmailService;
import com.clemble.casino.server.event.email.SystemEmailAddedEvent;
import com.clemble.casino.server.event.email.SystemEmailVerifiedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.notification.SystemNotificationService;

/**
 * Created by mavarazy on 12/6/14.
 */
public class SystemEmailAddedEventListener implements SystemEventListener<SystemEmailAddedEvent> {

    final private PlayerEmailRepository emailRepository;
    final private PlayerEmailService playerEmailService;
    final private SystemNotificationService systemNotificationService;

    public SystemEmailAddedEventListener(PlayerEmailService playerEmailService, PlayerEmailRepository emailRepository, SystemNotificationService systemNotificationService) {
        this.emailRepository = emailRepository;
        this.playerEmailService = playerEmailService;
        this.systemNotificationService = systemNotificationService;
    }

    @Override
    public void onEvent(SystemEmailAddedEvent event) {
        if (emailRepository.findOne(event.getPlayer()) == null) {
            // Step 1. Creating player email
            PlayerEmail playerEmail = new PlayerEmail(event.getPlayer(), event.getEmail());
            // Step 2. Saving player email
            if (event.getVerified()) {
                systemNotificationService.send(new SystemEmailVerifiedEvent(event.getPlayer()));
            } else {
                playerEmailService.requestVerification(playerEmail);
            }
            emailRepository.save(playerEmail);
        }
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
