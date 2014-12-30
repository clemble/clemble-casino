package com.clemble.casino.server.email.listener;

import com.clemble.casino.server.email.repository.PlayerEmailRepository;
import com.clemble.casino.server.email.service.ServerEmailSender;
import com.clemble.casino.server.event.email.SystemEmailSendRequestEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;

import com.clemble.casino.server.template.TemplateService;

/**
 * Created by mavarazy on 12/8/14.
 */
public class SystemEmailSendRequestEventListener implements SystemEventListener<SystemEmailSendRequestEvent>{

    final private ServerEmailSender emailSender;
    final private TemplateService templateService;
    final private PlayerEmailRepository emailRepository;

    public SystemEmailSendRequestEventListener(
        TemplateService templateService,
        ServerEmailSender emailSender,
        PlayerEmailRepository emailRepository
    ) {
        this.emailSender = emailSender;
        this.templateService = templateService;
        this.emailRepository = emailRepository;
    }

    @Override
    public void onEvent(SystemEmailSendRequestEvent event) {
        // Step 1. Fetching email
        String email = emailRepository.findOne(event.getPlayer()).getEmail();
        // Step 2. Generating text
        String text = templateService.produce(event.getTemplate(), event.getParams());
        // Step 3. Sending email
        emailSender.send(email, text);
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
