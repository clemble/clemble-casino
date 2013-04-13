package com.gogomaya.server.game.connection;

import com.gogomaya.server.game.action.GameTable;

public interface GameNotificationManager {

    public void notify(GameTable<?> gameTable);

}
