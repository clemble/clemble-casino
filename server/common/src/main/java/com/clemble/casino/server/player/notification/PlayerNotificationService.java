package com.clemble.casino.server.player.notification;

import java.util.Collection;

import com.clemble.casino.event.Event;
import com.clemble.casino.server.NotificationService;

public interface PlayerNotificationService<T extends Event> extends NotificationService {

    public boolean notify(final String path, final T gameEvent);

    public boolean notify(final String path, final Collection<T> gameEvent);

    public boolean notify(final Collection<String> players, final T gameEvent);

    public boolean notify(final Collection<String> players, final Collection<? extends T> gameEvent);

}
