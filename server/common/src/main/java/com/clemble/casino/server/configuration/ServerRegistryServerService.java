package com.clemble.casino.server.configuration;

import com.clemble.casino.server.player.notification.PlayerNotificationRegistry;

public interface ServerRegistryServerService {

    public PlayerNotificationRegistry getPlayerNotificationRegistry();

    public ServerLocation getPayment();

}
