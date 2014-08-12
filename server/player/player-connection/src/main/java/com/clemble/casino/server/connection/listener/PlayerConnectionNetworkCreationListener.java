package com.clemble.casino.server.connection.listener;

import com.clemble.casino.player.PlayerConnections;
import com.clemble.casino.server.connection.service.ServerPlayerConnectionService;
import com.clemble.casino.server.event.player.SystemPlayerCreatedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.connection.GraphPlayerConnections;
import org.springframework.social.connect.ConnectionKey;

import java.util.HashSet;

/**
 * Created by mavarazy on 7/4/14.
 */
public class PlayerConnectionNetworkCreationListener implements SystemEventListener<SystemPlayerCreatedEvent>{

    final private ServerPlayerConnectionService socialNetworkRepository;

    public PlayerConnectionNetworkCreationListener(ServerPlayerConnectionService socialNetworkRepository) {
        this.socialNetworkRepository = socialNetworkRepository;
    }

    @Override
    public void onEvent(SystemPlayerCreatedEvent event) {
        // Step 1. Generating player connections
        PlayerConnections connections = new PlayerConnections(event.getPlayer(), new HashSet<ConnectionKey>(), new HashSet<ConnectionKey>());
        // Step 2. Saving connections
        socialNetworkRepository.save(connections);
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
