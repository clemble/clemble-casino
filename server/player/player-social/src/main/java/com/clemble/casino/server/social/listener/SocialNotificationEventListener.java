package com.clemble.casino.server.social.listener;

import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.event.player.SystemPlayerSocialAddedEvent;
import com.clemble.casino.server.event.social.SystemSocialNotificationRequestEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.social.SocialConnectionAdapter;
import com.clemble.casino.server.social.SocialConnectionAdapterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

import java.util.List;

/**
 * Created by mavarazy on 12/6/14.
 */
public class SocialNotificationEventListener implements SystemEventListener<SystemSocialNotificationRequestEvent> {

    final private Logger LOG = LoggerFactory.getLogger(SocialNotificationEventListener.class);

    final private UsersConnectionRepository usersConnectionRepository;
    final private SocialConnectionAdapterRegistry adapterRegistry;

    public SocialNotificationEventListener(
        UsersConnectionRepository usersConnectionRepository,
        SocialConnectionAdapterRegistry adapterRegistry) {
        this.usersConnectionRepository = usersConnectionRepository;
        this.adapterRegistry = adapterRegistry;
    }


    @Override
    public void onEvent(SystemSocialNotificationRequestEvent event) {
        // Step 1. Fetching related user connections repository
        ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(event.getPlayer());
        // Step 2. Fetching related Connection
        List<Connection<?>> connections = connectionRepository.findConnections(event.getProviderId());
        // Step 3. Checking number of connections, and notifying on the latest connection
        if (connections.isEmpty()) {
            LOG.error("Player {} does not have connections to {}", event.getPlayer(), event.getProviderId());
        } else if(connections.size() > 1) {
            LOG.error("Player {} has too many connections to {} []", event.getPlayer(), event.getProviderId(), connections.stream().map((c) -> c.getKey()));
        } else {
            Connection<?> connection = connections.get(0);
            SocialConnectionAdapter adapter = adapterRegistry.getSocialAdapter(event.getProviderId());
            adapter.notify(connection, event.getNotification());
        }
    }

    @Override
    public String getChannel() {
        return SystemSocialNotificationRequestEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemSocialNotificationRequestEvent.CHANNEL + " > player:social";
    }

}
