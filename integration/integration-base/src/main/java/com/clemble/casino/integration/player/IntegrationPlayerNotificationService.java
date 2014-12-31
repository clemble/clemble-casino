package com.clemble.casino.integration.player;

import com.clemble.casino.notification.PlayerNotification;
import com.clemble.casino.player.service.PlayerNotificationService;
import com.clemble.casino.server.notification.controller.PlayerNotificationServiceController;

/**
 * Created by mavarazy on 11/29/14.
 */
public class IntegrationPlayerNotificationService implements PlayerNotificationService {

    final private String player;
    final private PlayerNotificationServiceController notificationService;

    public IntegrationPlayerNotificationService(String player, PlayerNotificationServiceController notificationService) {
        this.player = player;
        this.notificationService = notificationService;
    }


    @Override
    public PlayerNotification[] myNotifications() {
        return notificationService.myNotifications(player);
    }

    @Override
    public void delete(String key) {
        notificationService.delete(player, key);
    }

}
