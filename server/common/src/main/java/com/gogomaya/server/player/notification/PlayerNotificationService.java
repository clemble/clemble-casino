package com.gogomaya.server.player.notification;

import java.util.Collection;

import com.gogomaya.event.Event;

public interface PlayerNotificationService<T extends Event> {

    public boolean notify(final String player, final T gameEvent);

    public boolean notify(final Collection<String> players, final T gameEvent);

    public boolean notify(final Collection<String> players, final Collection<? extends T> gameEvent);

}
