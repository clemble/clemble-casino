package com.clemble.casino.server.notification.listener;

import com.clemble.casino.notification.PlayerNotification;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.player.event.PlayerDiscoveredConnectionEvent;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.event.notification.SystemNotificationAddEvent;
import com.clemble.casino.server.event.player.SystemPlayerDiscoveredConnectionEvent;
import com.clemble.casino.server.notification.ServerPlayerNotification;
import com.clemble.casino.server.notification.repository.PlayerNotificationRepository;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

/**
 * Created by mavarazy on 11/29/14.
 */
public class SystemNotificationAddEventListener implements SystemEventListener<SystemNotificationAddEvent> {

    final private PlayerConnectionService connectionService;
    final private PlayerNotificationRepository notificationRepository;
    final private ServerNotificationService serverNotificationService;

    public SystemNotificationAddEventListener(
        PlayerConnectionService connectionService,
        PlayerNotificationRepository notificationRepository,
        ServerNotificationService serverNotificationService) {
        this.connectionService = checkNotNull(connectionService);
        this.notificationRepository = checkNotNull(notificationRepository);
        this.serverNotificationService = checkNotNull(serverNotificationService);
    }

    @Override
    public void onEvent(SystemNotificationAddEvent event) {
        PlayerNotification notification = event.getNotification();
        // Step 1. Generating new notifications list
        Collection<ServerPlayerNotification> notifications = new ArrayList<ServerPlayerNotification>();
        notifications.add(new ServerPlayerNotification(null, notification.getPlayer(), notification, new Date()));
        // Step 2. Adding to other players notificaitons
        switch (event.getPrivacyRule()) {
            case me:
                break;
            case friends:
            case world:
                Set<String> connections = connectionService.getConnections(notification.getPlayer());
                for(String connection: connections)
                    notifications.add(new ServerPlayerNotification(null, connection, notification, new Date()));
                break;
        }
        // Step 3. Saving player notifications
        notificationRepository.save(notifications);
        // Step 4. Sending notification to all interested parties
        serverNotificationService.send(PlayerAwareUtils.toPlayerList(notifications), notification);
    }

    @Override
    public String getChannel() {
        return SystemNotificationAddEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemNotificationAddEvent.CHANNEL + " > player:notification";
    }

}
