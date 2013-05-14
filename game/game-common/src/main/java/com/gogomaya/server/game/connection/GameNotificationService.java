package com.gogomaya.server.game.connection;

import java.util.Collection;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.event.GameEvent;

public interface GameNotificationService<State extends GameState> {

    public void notify(final GameTable<State> gameTable);

    public void notify(final GameConnection connection, final GameEvent<State> gameEvent);

    public void notify(final GameConnection connection, final Collection<GameEvent<State>> gameEvent);

}
