package com.clemble.casino.server.connection.listener;

import com.clemble.casino.server.connection.service.PlayerGraphService;
import com.clemble.casino.server.event.player.SystemPlayerConnectionsFetchedEvent;
import com.clemble.casino.server.event.player.SystemPlayerDiscoveredConnectionEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.google.common.collect.Sets;

import java.util.Set;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

/**
 * Created by mavarazy on 7/4/14.
 */
public class PlayerConnectionNetworkPopulateListener implements SystemEventListener<SystemPlayerConnectionsFetchedEvent> {

    final private PlayerGraphService connectionService;
    final private SystemNotificationService notificationService;

    public PlayerConnectionNetworkPopulateListener(
        PlayerGraphService connectionService,
        SystemNotificationService notificationService) {
        this.connectionService = checkNotNull(connectionService);
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void onEvent(SystemPlayerConnectionsFetchedEvent event) {
        // Step 1. Finding appropriate PlayerConnections
        connectionService.addOwned(event.getPlayer(), event.getConnection());
        // Step 3. Adding connected connections
        Set<String> connected =  connectionService.getConnections(event.getPlayer());
        // Step 4. Checking for new connections
        Set<String> discoveredConnections = Sets.newHashSet(connectionService.getOwners(event.getConnections()));
        // Step 4.1. Removing all already connected
        discoveredConnections.removeAll(connected);
        // Step 5. Saving new connections
        for(String discovered : discoveredConnections) {
            connectionService.connect(event.getPlayer(), discovered);
            // Step 6. For all discovered connections send notification
            notificationService.send(new SystemPlayerDiscoveredConnectionEvent(event.getPlayer(), discovered));
        }
    }

    @Override
    public String getChannel() {
        return SystemPlayerConnectionsFetchedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerConnectionsFetchedEvent.CHANNEL + " > player:connection:populator";
    }

}
