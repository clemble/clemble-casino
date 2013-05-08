package com.gogomaya.server.game.connection;

public interface GameConnectionCache {

    public GameConnection getConnection(long sessionId);

}
