package com.clemble.casino.server.social;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collection;

import com.clemble.casino.server.event.SystemPlayerConnectionsFetchedEvent;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

import com.clemble.casino.server.event.SystemPlayerSocialAddedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.presence.SystemNotificationService;

public class SocialNetworkPopulatorEventListener implements SystemEventListener<SystemPlayerSocialAddedEvent> {

    final private SocialConnectionAdapterRegistry socialAdapterRegistry;
    final private UsersConnectionRepository usersConnectionRepository;
    final private SystemNotificationService notificationService;

    public SocialNetworkPopulatorEventListener(
        SocialConnectionAdapterRegistry socialAdapterRegistry,
        UsersConnectionRepository usersConnectionRepository,
        SystemNotificationService notificationService) {
        this.socialAdapterRegistry = checkNotNull(socialAdapterRegistry);
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
        Collection<ConnectionKey> connectionKeys = socialAdapter.fetchConnections(connection.getApi());
        // Step 7. Finding difference
        notificationService.notify(new SystemPlayerConnectionsFetchedEvent(event.getPlayer(), event.getConnection(), connectionKeys));
    }

    @Override
    public String getChannel() {
        return SystemPlayerSocialAddedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerSocialAddedEvent.CHANNEL + " > player:social:populator";
    }

}
