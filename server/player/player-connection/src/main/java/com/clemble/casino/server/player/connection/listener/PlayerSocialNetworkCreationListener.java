package com.clemble.casino.server.player.connection.listener;

import com.clemble.casino.server.event.SystemPlayerCreatedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.social.PlayerSocialNetwork;
import com.clemble.casino.server.repository.player.PlayerSocialNetworkRepository;

/**
 * Created by mavarazy on 7/4/14.
 */
public class PlayerSocialNetworkCreationListener implements SystemEventListener<SystemPlayerCreatedEvent>{

    final private PlayerSocialNetworkRepository socialNetworkRepository;

    public PlayerSocialNetworkCreationListener(PlayerSocialNetworkRepository socialNetworkRepository) {
        this.socialNetworkRepository = socialNetworkRepository;
    }

    @Override
    public void onEvent(SystemPlayerCreatedEvent event) {
        socialNetworkRepository.save(new PlayerSocialNetwork(event.getPlayer()));
    }

    @Override
    public String getChannel() {
        return SystemPlayerCreatedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerCreatedEvent.CHANNEL + " > player:connection";
    }
}
