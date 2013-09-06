package com.gogomaya.server.configuration;

import com.gogomaya.configuration.NotificationConfiguration;
import com.gogomaya.configuration.NotificationConfigurationService;
import com.gogomaya.configuration.NotificationHost;
import com.gogomaya.server.player.notification.PlayerNotificationRegistry;

public class SimpleNotificationConfigurationService implements NotificationConfigurationService {

    final private String user;
    final private String password;
    final private PlayerNotificationRegistry playerNotificationRegistry;

    public SimpleNotificationConfigurationService(String user, String password, PlayerNotificationRegistry playerNotificationRegistry) {
        this.user = user;
        this.password = password;
        this.playerNotificationRegistry = playerNotificationRegistry;
    }

    @Override
    public NotificationConfiguration get(long playerId) {
        String host = playerNotificationRegistry.findNotificationServer(playerId);
        NotificationHost rabbitHost = new NotificationHost(host, 5672);
        NotificationHost stompHost = new NotificationHost(host, 61613);
        NotificationHost sockjsHost = new NotificationHost(host, 15674);
        return new NotificationConfiguration(user, password, String.valueOf(playerId), rabbitHost, stompHost, sockjsHost);
    }

}
