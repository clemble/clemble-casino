package com.gogomaya.server.web.configuration;

import com.gogomaya.configuration.NotificationConfiguration;
import com.gogomaya.configuration.NotificationConfigurationService;
import com.gogomaya.configuration.NotificationHost;

public class SimpleNotificationConfigurationService implements NotificationConfigurationService {

    final private String user;
    final private String password;
    final private String host;

    public SimpleNotificationConfigurationService(String user, String password, String host) {
        this.user = user;
        this.password = password;
        this.host = host;
    }

    @Override
    public NotificationConfiguration get(long playerId) {
        NotificationHost rabbitHost = new NotificationHost(host, 5672);
        NotificationHost stompHost = new NotificationHost(host, 61613);
        NotificationHost sockjsHost = new NotificationHost(host, 15674);
        return new NotificationConfiguration(user, password, String.valueOf(playerId), rabbitHost, stompHost, sockjsHost);
    }

}
