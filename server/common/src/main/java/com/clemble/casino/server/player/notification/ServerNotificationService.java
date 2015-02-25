package com.clemble.casino.server.player.notification;

import java.util.Collection;

import com.clemble.casino.event.Event;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.NotificationService;

public interface ServerNotificationService extends NotificationService {

    public <T extends PlayerAware & Event> boolean send(T event);

    public <T extends PlayerAware & Event> boolean send(Collection<T> events);

    public <T extends Event> boolean send(final String path, final T event);

    public <T extends Event> boolean send(final String path, final Collection<T> event);

    public <T extends Event> boolean send(final Collection<String> players, final T event);

    public <T extends Event> boolean send(final Collection<String> players, final Collection<? extends T> event);

}
