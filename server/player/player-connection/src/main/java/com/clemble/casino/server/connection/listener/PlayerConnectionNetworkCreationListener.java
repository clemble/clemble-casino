package com.clemble.casino.server.connection.listener;

import com.clemble.casino.server.event.player.SystemPlayerCreatedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.connection.PlayerConnectionNetwork;
import com.clemble.casino.server.connection.repository.PlayerConnectionNetworkRepository;

/**
 * Created by mavarazy on 7/4/14.
 */
public class PlayerConnectionNetworkCreationListener implements SystemEventListener<SystemPlayerCreatedEvent>{

    final private PlayerConnectionNetworkRepository socialNetworkRepository;

    public PlayerConnectionNetworkCreationListener(PlayerConnectionNetworkRepository socialNetworkRepository) {
        this.socialNetworkRepository = socialNetworkRepository;
    }

    @Override
    public void onEvent(SystemPlayerCreatedEvent event) {
        socialNetworkRepository.save(new PlayerConnectionNetwork(event.getPlayer()));
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
