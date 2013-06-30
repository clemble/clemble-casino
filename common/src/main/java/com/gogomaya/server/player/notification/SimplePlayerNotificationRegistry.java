package com.gogomaya.server.player.notification;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.ServerRegistry;

public class SimplePlayerNotificationRegistry implements PlayerNotificationRegistry {

    final private ServerRegistry serverRegistry;

    public SimplePlayerNotificationRegistry(ServerRegistry serverRegistry) {
        this.serverRegistry = checkNotNull(serverRegistry);
    }

    @Override
    public String findNotificationServer(long playerId) {
        return serverRegistry.find(playerId);
    }

}
