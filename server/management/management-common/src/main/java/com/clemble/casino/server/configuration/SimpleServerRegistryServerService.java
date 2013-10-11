package com.clemble.casino.server.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.server.player.notification.PlayerNotificationRegistry;

public class SimpleServerRegistryServerService implements ServerRegistryServerService {

    final private PlayerNotificationRegistry playerNotificationRegistry;
    final private ServerLocation paymentEndpointRegistry;

    public SimpleServerRegistryServerService(PlayerNotificationRegistry playerNotificationRegistry,
            ServerLocation paymentEndpoint) {
        this.playerNotificationRegistry = checkNotNull(playerNotificationRegistry);
        this.paymentEndpointRegistry = checkNotNull(paymentEndpoint);
    }

    @Override
    public PlayerNotificationRegistry getPlayerNotificationRegistry() {
        return playerNotificationRegistry;
    }

    @Override
    public ServerLocation getPayment() {
        return paymentEndpointRegistry;
    }

}
