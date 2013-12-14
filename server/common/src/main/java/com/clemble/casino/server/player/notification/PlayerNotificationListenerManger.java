package com.clemble.casino.server.player.notification;

import java.util.Collection;

import com.clemble.casino.server.event.SystemEvent;

public interface PlayerNotificationListenerManger<T extends SystemEvent> {

    public void subscribe(final String player, final SystemEventListener<T> messageListener);

    public void subscribe(final Collection<String> players, final SystemEventListener<T> messageListener);

    public void unsubscribe(final String player, final SystemEventListener<T> messageListener);

    public void unsubscribe(final Collection<String> players, final SystemEventListener<T> messageListener);

}
