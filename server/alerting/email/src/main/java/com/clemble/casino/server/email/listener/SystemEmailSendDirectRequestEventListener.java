package com.clemble.casino.server.email.listener;

import com.clemble.casino.server.email.service.ServerEmailSender;
import com.clemble.casino.server.event.email.SystemEmailSendDirectRequestEvent;
import com.clemble.casino.server.event.email.SystemEmailSendRequestEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.template.TemplateService;

/**
 * Created by mavarazy on 2/2/15.
 */
public class SystemEmailSendDirectRequestEventListener implements SystemEventListener<SystemEmailSendDirectRequestEvent> {

    final private ServerEmailSender emailSender;
    final private TemplateService templateService;

    public SystemEmailSendDirectRequestEventListener(ServerEmailSender emailSender, TemplateService templateService){
        this.emailSender = emailSender;
        this.templateService = templateService;
    }

    @Override
    public void onEvent(SystemEmailSendDirectRequestEvent event) {
        // Step 1. Generating text
        String text = templateService.produce(event.getTemplate(), event.getParams());
        // Step 2. Sending message
        emailSender.send(event.getEmail(), text);
    }

    @Override
    public String getChannel() {
        return SystemEmailSendDirectRequestEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemEmailSendDirectRequestEvent.CHANNEL + " > player:email";
    }

}
