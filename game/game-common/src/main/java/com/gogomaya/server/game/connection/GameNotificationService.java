package com.gogomaya.server.game.connection;

import java.util.Collection;

import com.gogomaya.server.event.GogomayaEvent;
import com.gogomaya.server.game.action.GameState;

public interface GameNotificationService<State extends GameState> {

    public void notify(final GameConnection connection, final GogomayaEvent gameEvent);

    public void notify(final GameConnection connection, final Collection<? extends GogomayaEvent> gameEvent);

}
