package com.gogomaya.server.player.notification;

import java.util.Collection;

import com.gogomaya.event.Event;

public interface PlayerNotificationService<T extends Event> {

    public boolean notify(final long playerId, final T gameEvent);

    public boolean notify(final Collection<Long> playerIds, final T gameEvent);

    public boolean notify(final Collection<Long> playerIds, final Collection<? extends T> gameEvent);

}
