package com.clemble.casino.server.connection.listener;

import com.clemble.casino.player.PlayerConnections;
import com.clemble.casino.server.connection.service.ServerPlayerConnectionService;
import com.clemble.casino.server.event.player.SystemPlayerConnectionsFetchedEvent;
import com.clemble.casino.server.event.player.SystemPlayerDiscoveredConnectionEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.WebMapping;
import org.springframework.social.connect.ConnectionKey;

import java.util.HashSet;
import java.util.Set;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

/**
 * Created by mavarazy on 7/4/14.
 */
public class PlayerConnectionNetworkPopulateListener implements SystemEventListener<SystemPlayerConnectionsFetchedEvent> {

    final private ServerPlayerConnectionService connectionService;
    final private SystemNotificationService notificationService;

    public PlayerConnectionNetworkPopulateListener(
        ServerPlayerConnectionService connectionService,
        SystemNotificationService notificationService) {
        this.connectionService = checkNotNull(connectionService);
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void onEvent(SystemPlayerConnectionsFetchedEvent event) {
        // Step 1. Finding appropriate PlayerConnections
        PlayerConnections playerConnections = connectionService.getConnections(event.getPlayer());
        // Step 2. Adding owned Connections
        Set<ConnectionKey> owned = new HashSet<>(playerConnections.getOwned());
        owned.add(event.getConnection());
        // Step 3. Adding connected connections
        Set<ConnectionKey> connected =  new HashSet<>(playerConnections.getConnected());
        connected.addAll(event.getConnections());
        // Step 4. Checking for new connections
        Set<ConnectionKey> discoveredConnections = new HashSet<>();
        for(PlayerConnections player: connectionService.getOwners(event.getConnections()))
            discoveredConnections.add(new ConnectionKey(WebMapping.PROVIDER_ID, player.getPlayer()));
        // Step 4.1. Removing all already connected
        discoveredConnections.removeAll(connected);
        // Step 4.2. Merging to already connected
        connected.addAll(discoveredConnections);
        // Step 5. Saving new connections
        if(owned.size() != playerConnections.getOwned().size() || connected.size() != playerConnections.getConnected().size()) {
            connectionService.save(new PlayerConnections(playerConnections.getPlayer(), owned, connected));
            // Step 6. For all discovered connections send notification
            for (ConnectionKey discovered : discoveredConnections)
                notificationService.send(new SystemPlayerDiscoveredConnectionEvent(event.getPlayer(), discovered.getProviderUserId()));
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
