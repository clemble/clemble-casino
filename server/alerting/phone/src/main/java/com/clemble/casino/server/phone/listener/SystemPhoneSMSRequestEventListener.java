package com.clemble.casino.server.phone.listener;

import com.clemble.casino.server.event.phone.SystemPhoneSMSSendRequestEvent;
import com.clemble.casino.server.phone.PlayerPhone;
import com.clemble.casino.server.phone.repository.PlayerPhoneRepository;
import com.clemble.casino.server.phone.service.ServerPlayerPhoneService;
import com.clemble.casino.server.phone.service.ServerSMSSender;
import com.clemble.casino.server.player.notification.SystemEventListener;

/**
 * Created by mavarazy on 12/8/14.
 */
public class SystemPhoneSMSRequestEventListener implements SystemEventListener<SystemPhoneSMSSendRequestEvent>{

    final private ServerSMSSender smsSender;
    final private PlayerPhoneRepository phoneRepository;

    public SystemPhoneSMSRequestEventListener(ServerSMSSender smsSender, PlayerPhoneRepository phoneRepository) {
        this.smsSender = smsSender;
        this.phoneRepository = phoneRepository;
    }

    @Override
    public void onEvent(SystemPhoneSMSSendRequestEvent event) {
        // Step 1. Fetching phone
        PlayerPhone phone = phoneRepository.findOne(event.getPlayer());
        // Step 2. Sending SMS
        smsSender.send(phone.getPhone(), event.getText());
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
