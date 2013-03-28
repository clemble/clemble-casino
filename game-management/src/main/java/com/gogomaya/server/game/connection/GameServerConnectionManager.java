package com.gogomaya.server.game.connection;

import org.springframework.stereotype.Component;

import com.gogomaya.server.game.resource.connection.GameServerConnection;

@Component
public class GameServerConnectionManager {

    public GameServerConnection reserve() {
        return new GameServerConnection("notify", "publish");
    }

}
