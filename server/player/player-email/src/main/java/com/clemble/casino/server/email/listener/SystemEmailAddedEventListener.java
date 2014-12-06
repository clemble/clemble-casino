package com.clemble.casino.server.email.listener;

import com.clemble.casino.server.email.PlayerEmail;
import com.clemble.casino.server.email.repository.PlayerEmailRepository;
import com.clemble.casino.server.event.email.SystemEmailAddedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;

/**
 * Created by mavarazy on 12/6/14.
 */
public class SystemEmailAddedEventListener implements SystemEventListener<SystemEmailAddedEvent> {

    final private PlayerEmailRepository emailRepository;

    public SystemEmailAddedEventListener(PlayerEmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public void onEvent(SystemEmailAddedEvent event) {
        // Step 1. Creating player email
        // TODO distinguish verified and unverified email
        PlayerEmail playerEmail = new PlayerEmail(event.getPlayer(), event.getEmail());
        // Step 2. Saving player email
        emailRepository.save(playerEmail);
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
