package com.gogomaya.server.game.connection;

public class GameConnection {

    private GameServerConnection serverConnection;

    private long routingKey;

    public long getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(long routingKey) {
        this.routingKey = routingKey;
    }

    public GameServerConnection getServerConnection() {
        return serverConnection;
    }

    public void setServerConnection(GameServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

}
