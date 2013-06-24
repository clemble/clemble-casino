package com.gogomaya.server.game.notification;

import java.util.Collection;

import com.gogomaya.server.event.Event;
import com.gogomaya.server.game.GameState;

public interface GameNotificationService<State extends GameState> {

    public void notify(final long playerId, final Event gameEvent);
    
    public void notify(final Collection<Long> playerIds, final Event gameEvent);

    public void notify(final Collection<Long> playerIds, final Collection<? extends Event> gameEvent);

}
