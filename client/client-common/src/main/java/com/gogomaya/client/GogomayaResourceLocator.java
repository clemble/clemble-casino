package com.gogomaya.client;

import com.gogomaya.client.player.service.PlayerSecurityClientService;
import com.gogomaya.configuration.NotificationConfiguration;

public interface GogomayaResourceLocator {

    // Security service location
    public PlayerSecurityClientService getSecurityService();

    // Notification server endpoint
    public NotificationConfiguration getNotificationServerConfigurations();

    // Player related endpoint
    public String getPlayerProfileEndpoint();

    // Player session endpoint
    public String getPlayerSessionEndpoint();

    // Payment related endpoint
    public String getPaymentEndpoint();

    // Game construction related endpoint
    public String getGameConstructionEndpoint();

    // Game action related endpoint
    public String getGameActionEndpoint(long sessionId);

}
