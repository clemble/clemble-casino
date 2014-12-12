package com.clemble.casino.server.profile.listener;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.event.PlayerProfileChangedEvent;
import com.clemble.casino.player.service.PlayerNotificationService;
import com.clemble.casino.server.event.email.SystemEmailVerifiedEvent;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.profile.repository.PlayerProfileRepository;
import org.springframework.social.connect.ConnectionKey;

/**
 * Created by mavarazy on 12/12/14.
 */
public class PlayerProfileEmailVerifiedEventListener implements SystemEventListener<SystemEmailVerifiedEvent> {

    final private PlayerProfileRepository profileRepository;
    final private ServerNotificationService notificationService;

    public PlayerProfileEmailVerifiedEventListener(
        PlayerProfileRepository profileRepository,
        ServerNotificationService notificationService
    ) {
        this.profileRepository = profileRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void onEvent(SystemEmailVerifiedEvent event) {
        // Step 1. Fetching player profile
        PlayerProfile profile = profileRepository.findOne(event.getPlayer());
        // Step 2. Adding new connection to SocialConnections
        profile.addSocialConnection(new ConnectionKey("email", "verified"));
        // Step 3. Saving updated player profile
        profileRepository.save(profile);
        // Step 4. Sending profile changed event
        notificationService.send(new PlayerProfileChangedEvent(profile.getPlayer(), profile));
    }

    @Override
    public String getChannel() {
        return SystemEmailVerifiedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemEmailVerifiedEvent.CHANNEL + " > player:profile";
    }
}
