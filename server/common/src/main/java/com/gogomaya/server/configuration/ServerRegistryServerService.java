package com.gogomaya.server.configuration;

import com.gogomaya.server.player.notification.PlayerNotificationRegistry;

public interface ServerRegistryServerService {

    public PlayerNotificationRegistry getPlayerNotificationRegistry();

    public ServerLocation getPayment();

}
