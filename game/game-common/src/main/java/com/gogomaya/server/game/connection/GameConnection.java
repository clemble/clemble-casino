package com.gogomaya.server.game.connection;

public class GameConnection {

    private GameServerConnection serverConnection;

    private long routingKey;

    public long getRoutingKey() {
        return routingKey;
    }

    public GameConnection setRoutingKey(long routingKey) {
        this.routingKey = routingKey;
        return this;
    }

    public GameServerConnection getServerConnection() {
        return serverConnection;
    }

    public GameConnection setServerConnection(GameServerConnection serverConnection) {
        this.serverConnection = serverConnection;
        return this;
    }

}
