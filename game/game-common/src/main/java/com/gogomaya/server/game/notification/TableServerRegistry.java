package com.gogomaya.server.game.notification;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.notification.ServerRegistry;

public class TableServerRegistry {

    final private ServerRegistry SERVER_REGISTRY;

    public TableServerRegistry(ServerRegistry serverRegistry) {
        this.SERVER_REGISTRY = checkNotNull(serverRegistry);
    }

    public String findServer(long tableId) {
        return SERVER_REGISTRY.find(tableId);
    }

    public <State extends GameState> GameTable<State> specifyServer(GameTable<State> table) {
        if (table == null)
            return table;

        table.setServer(findServer(table.getTableId()));

        return table;
    }

}
