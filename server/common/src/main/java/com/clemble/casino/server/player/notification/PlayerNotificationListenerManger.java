package com.clemble.casino.server.player.notification;

import java.util.Collection;

import com.clemble.casino.event.Event;

public interface PlayerNotificationListenerManger<T extends Event> {

    public void subscribe(final String player, final PlayerNotificationListener<T> messageListener);

    public void subscribe(final Collection<String> players, final PlayerNotificationListener<T> messageListener);

    public void unsubscribe(final String player, final PlayerNotificationListener<T> messageListener);

    public void unsubscribe(final Collection<String> players, final PlayerNotificationListener<T> messageListener);

}
