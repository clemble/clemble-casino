package com.clemble.casino.server.social;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

import com.clemble.casino.server.event.SystemPlayerSocialAddedEvent;
import com.clemble.casino.server.event.SystemPlayerDiscoveredConnectionEvent;
import com.clemble.casino.server.player.PlayerSocialNetwork;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.presence.SystemNotificationService;
import com.clemble.casino.server.repository.player.PlayerSocialNetworkRepository;
import com.google.common.collect.ImmutableList;

public class SocialNetworkPopulatorEventListener implements SystemEventListener<SystemPlayerSocialAddedEvent> {

    final private SocialConnectionAdapterRegistry socialAdapterRegistry;
    final private UsersConnectionRepository usersConnectionRepository;
    final private PlayerSocialNetworkRepository socialNetworkRepository;
    final private SystemNotificationService notificationService;

    public SocialNetworkPopulatorEventListener(SocialConnectionAdapterRegistry socialAdapterRegistry, UsersConnectionRepository usersConnectionRepository,
            PlayerSocialNetworkRepository socialNetworkRepository, SystemNotificationService notificationService) {
        this.socialAdapterRegistry = checkNotNull(socialAdapterRegistry);
        this.socialNetworkRepository = checkNotNull(socialNetworkRepository);
        this.notificationService = checkNotNull(notificationService);
        this.usersConnectionRepository = checkNotNull(usersConnectionRepository);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void onEvent(SystemPlayerSocialAddedEvent event) {
        // Step 1. Finding appropriate SocialConnectionAdapter
        SocialConnectionAdapter socialAdapter = socialAdapterRegistry.getSocialAdapter(event.getConnection().getProviderId());
        // Step 2. Fetching connection
        ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(event.getPlayer());
        Connection<?> connection = connectionRepository.getConnection(event.getConnection());
        // Step 3. Fetching PlayerSocialNetwork and existing connections
        Collection<PlayerSocialNetwork> connectionsBefore = ImmutableList.copyOf(socialNetworkRepository.findRelations(event.getPlayer()));
        //TODO Switch to Neo4jTemplate, otherwise this kills saved relationships
        PlayerSocialNetwork socialNetwork = new PlayerSocialNetwork(event.getPlayer());
        socialNetwork.addOwned(event.getConnection());
        // Step 4. Updating socialNetwork with new connections
        socialNetwork = socialAdapter.enrichPlayerNetwork(socialNetwork, connection.getApi());
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
        return SystemPlayerSocialAddedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return "player.social.populator";
    }

}
