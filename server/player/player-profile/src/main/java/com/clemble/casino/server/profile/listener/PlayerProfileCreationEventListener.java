package com.clemble.casino.server.profile.listener;

import com.clemble.casino.server.event.player.SystemPlayerCreatedEvent;
import com.clemble.casino.server.event.player.SystemPlayerProfileRegistered;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.profile.repository.PlayerProfileRepository;

/**
 * Created by mavarazy on 7/4/14.
 */
public class PlayerProfileCreationEventListener implements SystemEventListener<SystemPlayerProfileRegistered>{

    final private PlayerProfileRepository profileRepository;
    final private SystemNotificationService notificationService;

    public PlayerProfileCreationEventListener(PlayerProfileRepository profileRepository, SystemNotificationService notificationService) {
        this.profileRepository = profileRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void onEvent(SystemPlayerProfileRegistered event) {
        // Step 1. Saving player profile
        profileRepository.save(event.getPlayerProfile());
        // Step 2. Sending notification to outside world
        notificationService.notify(new SystemPlayerCreatedEvent(event.getPlayer()));
    }

    @Override
    public String getChannel() {
        return SystemPlayerProfileRegistered.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerProfileRegistered.CHANNEL + " > player:profile:registration";
    }
}
