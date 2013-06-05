package com.gogomaya.server.game.notification;

import java.util.Collection;

import com.gogomaya.server.event.GogomayaEvent;
import com.gogomaya.server.game.action.GameState;

public interface GameNotificationService<State extends GameState> {

    public void notify(final long playerId, final GogomayaEvent gameEvent);
    
    public void notify(final Collection<Long> playerIds, final GogomayaEvent gameEvent);

    public void notify(final Collection<Long> playerIds, final Collection<? extends GogomayaEvent> gameEvent);

}
