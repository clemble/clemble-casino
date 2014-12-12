package com.clemble.casino.server.profile.listener;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.event.PlayerProfileChangedEvent;
import com.clemble.casino.server.event.player.SystemPlayerProfileRegisteredEvent;
import com.clemble.casino.server.event.player.SystemPlayerSocialAddedEvent;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.profile.repository.PlayerProfileRepository;

/**
 * Created by mavarazy on 12/5/14.
 */
public class PlayerProfileSocialAddedEventListener implements SystemEventListener<SystemPlayerSocialAddedEvent>{

    final private PlayerProfileRepository profileRepository;
    final private ServerNotificationService notificationService;

    public PlayerProfileSocialAddedEventListener(
        PlayerProfileRepository profileRepository,
        ServerNotificationService notificationService
    ) {
        this.profileRepository = profileRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void onEvent(SystemPlayerSocialAddedEvent event) {
        // Step 1. Fetching player profile
        PlayerProfile profile = profileRepository.findOne(event.getPlayer());
        // Step 2. Adding new connection to SocialConnections
        profile.addSocialConnection(event.getConnection());
        // Step 3. Saving updated player profile
        profileRepository.save(profile);
        // Step 4. Sending profile changed event
        notificationService.send(new PlayerProfileChangedEvent(profile.getPlayer(), profile));
    }

    @Override
    public String getChannel() {
        return SystemPlayerSocialAddedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerSocialAddedEvent.CHANNEL + " > player:profile";
    }
}
