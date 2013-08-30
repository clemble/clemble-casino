package com.gogomaya.server.player.notification;

import java.util.Collection;

import com.gogomaya.event.Event;

public interface PlayerNotificationService {

    public void notify(final long playerId, final Event gameEvent);
    
    public void notify(final Collection<Long> playerIds, final Event gameEvent);

    public void notify(final Collection<Long> playerIds, final Collection<? extends Event> gameEvent);

}
