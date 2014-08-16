package com.clemble.casino.server.profile.listener;

import com.clemble.casino.server.event.player.SystemPlayerImageChanged;
import com.clemble.casino.server.event.player.SystemPlayerProfileRegistered;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.profile.PlayerImageRedirect;
import com.clemble.casino.server.profile.repository.PlayerImageRedirectRepository;

/**
 * Created by mavarazy on 8/16/14.
 */
public class PlayerImageChangedEventListener implements SystemEventListener<SystemPlayerImageChanged> {

    final private PlayerImageRedirectRepository imageRedirectRepository;

    public PlayerImageChangedEventListener(PlayerImageRedirectRepository imageRedirectRepository) {
        this.imageRedirectRepository = imageRedirectRepository;
    }

    @Override
    public void onEvent(SystemPlayerImageChanged event) {
        // Step 1. Updating image redirect
        imageRedirectRepository.save(new PlayerImageRedirect(event.getPlayer(), event.getRedirect()));
    }

    @Override
    public String getChannel() {
        return SystemPlayerImageChanged.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerImageChanged.CHANNEL + " > player:image:redirect";
    }
}
