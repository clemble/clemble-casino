package com.clemble.casino.server.email.listener;

import com.clemble.casino.server.email.repository.PlayerEmailRepository;
import com.clemble.casino.server.email.service.ServerEmailSender;
import com.clemble.casino.server.event.email.SystemEmailSendRequestEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;

/**
 * Created by mavarazy on 12/8/14.
 */
public class SystemEmailSendRequestEventListener implements SystemEventListener<SystemEmailSendRequestEvent>{

    final private ServerEmailSender emailSender;
    final private PlayerEmailRepository emailRepository;

    public SystemEmailSendRequestEventListener(
        ServerEmailSender emailSender,
        PlayerEmailRepository emailRepository
    ) {
        this.emailSender = emailSender;
        this.emailRepository = emailRepository;
    }

    @Override
    public void onEvent(SystemEmailSendRequestEvent event) {
        // Step 1. Fetching email
        String email = emailRepository.findOne(event.getPlayer()).getEmail();
        // Step 2. Sending email
        emailSender.send(email, event.getText());
    }

    @Override
    public String getChannel() {
        return SystemEmailSendRequestEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemEmailSendRequestEvent.CHANNEL + " > player:email";
    }
}
