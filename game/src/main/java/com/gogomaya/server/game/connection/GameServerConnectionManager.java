package com.gogomaya.server.game.connection;

import org.springframework.stereotype.Component;

import com.gogomaya.server.game.resource.connection.GameServerConnection;

@Component
public class GameServerConnectionManager {

    public GameServerConnection reserve() {
        return new GameServerConnection("ec2-50-16-93-157.compute-1.amazonaws.com", "publish");
    }

}
