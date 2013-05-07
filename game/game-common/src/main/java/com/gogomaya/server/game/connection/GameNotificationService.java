package com.gogomaya.server.game.connection;

import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.event.GameEvent;

public interface GameNotificationService {

    public void notify(final GameTable<?> gameTable);

    public void notify(final GameConnection connection, final GameEvent<?> gameEvent);

}
