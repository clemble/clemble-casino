package com.clemble.casino.server.configuration;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.configuration.NotificationConfiguration;
import com.clemble.casino.configuration.NotificationConfigurationService;
import com.clemble.casino.configuration.NotificationHost;

public class SimpleNotificationConfigurationService implements NotificationConfigurationService {

    final private String user;
    final private String password;
    final private ServerRegistry notificationRegistry;

    public SimpleNotificationConfigurationService(String user, String password, ServerRegistry playerNotificationRegistry) {
        this.user = user;
        this.password = password;
        this.notificationRegistry = playerNotificationRegistry;
    }

    @Override
    public NotificationConfiguration get(String playerId) {
        String host = notificationRegistry.findById(playerId);
        NotificationHost rabbitHost = new NotificationHost(host, 5672);
        NotificationHost stompHost = new NotificationHost(host, 61613);
        NotificationHost sockjsHost = new NotificationHost(host, 15674);
        return new NotificationConfiguration(user, password, playerId, rabbitHost, stompHost, sockjsHost);
    }

}
