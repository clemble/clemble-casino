package com.clemble.casino.server.phone.listener;

import com.clemble.casino.server.event.phone.SystemPhoneSMSSendRequestEvent;
import com.clemble.casino.server.phone.ServerPlayerPhone;
import com.clemble.casino.server.phone.repository.PlayerPhoneRepository;
import com.clemble.casino.server.phone.service.ServerSMSSender;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.template.TemplateService;

/**
 * Created by mavarazy on 12/8/14.
 */
public class SystemPhoneSMSRequestEventListener implements SystemEventListener<SystemPhoneSMSSendRequestEvent>{

    final private ServerSMSSender smsSender;
    final private TemplateService templateService;
    final private PlayerPhoneRepository phoneRepository;

    public SystemPhoneSMSRequestEventListener(TemplateService templateService, ServerSMSSender smsSender, PlayerPhoneRepository phoneRepository) {
        this.smsSender = smsSender;
        this.templateService = templateService;
        this.phoneRepository = phoneRepository;
    }

    @Override
    public void onEvent(SystemPhoneSMSSendRequestEvent event) {
        // Step 1. Fetching phone
        ServerPlayerPhone phone = phoneRepository.findOne(event.getPlayer());
        // Step 2. Generating text
        String text = templateService.produce(event.getTemplate(), event.getParams());
        // Step 3. Sending SMS
        smsSender.send(phone.getPhone(), text);
    }

    @Override
    public String getChannel() {
        return SystemPhoneSMSSendRequestEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPhoneSMSSendRequestEvent.CHANNEL + " > alerting:phone";
    }
}
