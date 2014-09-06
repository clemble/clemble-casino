package com.clemble.casino.server.profile.listener;

import com.clemble.casino.server.event.player.SystemPlayerImageChangedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.profile.PlayerImageRedirect;
import com.clemble.casino.server.profile.repository.PlayerImageRedirectRepository;

/**
 * Created by mavarazy on 8/16/14.
 */
public class PlayerImageChangedEventListener implements SystemEventListener<SystemPlayerImageChangedEvent> {

    final private PlayerImageRedirectRepository imageRedirectRepository;

    public PlayerImageChangedEventListener(PlayerImageRedirectRepository imageRedirectRepository) {
        this.imageRedirectRepository = imageRedirectRepository;
    }

    @Override
    public void onEvent(SystemPlayerImageChangedEvent event) {
        // Step 1. Updating image redirect
        imageRedirectRepository.save(new PlayerImageRedirect(event.getPlayer(), event.getRedirect(), event.getSmallImage()));
    }

    @Override
    public String getChannel() {
        return SystemPlayerImageChangedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerImageChangedEvent.CHANNEL + " > player:image:redirect";
    }
}
