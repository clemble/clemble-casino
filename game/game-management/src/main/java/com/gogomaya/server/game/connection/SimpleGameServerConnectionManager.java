package com.gogomaya.server.game.connection;

public class SimpleGameServerConnectionManager implements GameServerConnectionManager {

    final String notificationUrl;
    final String publishUrl;

    public SimpleGameServerConnectionManager(String notificationUrl, String publishUrl) {
        this.notificationUrl = notificationUrl;
        this.publishUrl = publishUrl;
    }

    @Override
    public GameServerConnection reserve() {
        return new GameServerConnection(notificationUrl, publishUrl);
    }

}
