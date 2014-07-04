package com.clemble.casino.server.player.connection.listener;

import com.clemble.casino.server.event.SystemPlayerConnectionsFetchedEvent;
import com.clemble.casino.server.event.SystemPlayerDiscoveredConnectionEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.presence.SystemNotificationService;
import com.clemble.casino.server.player.social.PlayerSocialNetwork;
import com.clemble.casino.server.repository.player.PlayerSocialNetworkRepository;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

/**
 * Created by mavarazy on 7/4/14.
 */
public class PlayerSocialNetworkConnectionCreatorListener implements SystemEventListener<SystemPlayerConnectionsFetchedEvent> {

    final private PlayerSocialNetworkRepository socialNetworkRepository;
    final private SystemNotificationService notificationService;

    public PlayerSocialNetworkConnectionCreatorListener(
        PlayerSocialNetworkRepository socialNetworkRepository,
        SystemNotificationService notificationService) {
        this.socialNetworkRepository = checkNotNull(socialNetworkRepository);
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void onEvent(SystemPlayerConnectionsFetchedEvent event) {
        // Step 1. Finding appropriate SocialConnectionAdapter
        Collection<PlayerSocialNetwork> connectionsBefore = ImmutableList.copyOf(socialNetworkRepository.findRelations(event.getPlayer()));
        //TODO Switch to Neo4jTemplate, otherwise this kills saved relationships
        PlayerSocialNetwork socialNetwork = new PlayerSocialNetwork(event.getPlayer());
        socialNetwork.addOwned(event.getConnection());
        // Step 4. Updating socialNetwork with new connections
        socialNetwork.addConnections(event.getConnections());
        // Step 5. Saving social network repository
        socialNetworkRepository.save(socialNetwork);
        // Step 6. Fetching new connections
        Collection<PlayerSocialNetwork> connectionsAfter = new ArrayList<>(ImmutableList.copyOf(socialNetworkRepository.findRelations(event.getPlayer())));
        // Step 7. Finding difference
        connectionsAfter.removeAll(connectionsBefore);
        for (PlayerSocialNetwork newConnection : connectionsAfter)
            notificationService.notify(new SystemPlayerDiscoveredConnectionEvent(event.getPlayer(), newConnection.getPlayer()));
    }

    @Override
    public String getChannel() {
        return SystemPlayerConnectionsFetchedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return "player:connection:populator";
    }

}
