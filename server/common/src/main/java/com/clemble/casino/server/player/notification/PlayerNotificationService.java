package com.clemble.casino.server.player.notification;

import java.util.Collection;

import com.clemble.casino.event.Event;

public interface PlayerNotificationService<T extends Event> {

    public boolean notify(final String player, final T gameEvent);

    public boolean notify(final Collection<String> players, final T gameEvent);

    public boolean notify(final Collection<String> players, final Collection<? extends T> gameEvent);

}
