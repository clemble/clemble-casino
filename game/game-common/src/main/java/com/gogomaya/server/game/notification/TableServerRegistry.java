package com.gogomaya.server.game.notification;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.notification.ServerRegistry;

public class TableServerRegistry {

    final private ServerRegistry SERVER_REGISTRY;

    public TableServerRegistry(ServerRegistry serverRegistry) {
        this.SERVER_REGISTRY = checkNotNull(serverRegistry);
    }

    public String findServer(long tableId) {
        return SERVER_REGISTRY.find(tableId);
    }

}
