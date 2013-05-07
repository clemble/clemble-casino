package com.gogomaya.server.game.connection;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;

public class SimpleGameServerConnectionManager implements GameServerConnectionManager {

    final String notificationUrl;
    final String publishUrl;

    public SimpleGameServerConnectionManager(String notificationUrl, String publishUrl) {
        this.notificationUrl = notificationUrl;
        this.publishUrl = publishUrl;
    }

    @Override
    public GameServerConnection reserve(GameTable<? extends GameState> table) {
        return new GameServerConnection(notificationUrl, publishUrl);
    }

}
