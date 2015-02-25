package com.clemble.casino.server.notification.listener;

import com.clemble.casino.notification.PlayerNotification;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.event.notification.SystemNotificationAddEvent;
import com.clemble.casino.server.notification.repository.PlayerNotificationRepository;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemEventListener;

import java.util.ArrayList;
import java.util.Collection;
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
        // Step 0. Don't store any notifications for the player
        if (PlayerAware.DEFAULT_PLAYER.equals(event.getNotification().getPlayer()))
            return;
        PlayerNotification notification = event.getNotification();
        // Step 1. Generating new notifications list
        notificationRepository.save(notification);
        // Step 2. Adding to other players notifications
        // Step 3. Saving player notifications
        // Step 4. Sending notification to all interested parties
        serverNotificationService.send(notification);
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
