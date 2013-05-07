package com.gogomaya.server.game.connection;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;

public interface GameServerConnectionManager {

    public GameServerConnection reserve(GameTable<? extends GameState> table);

}
