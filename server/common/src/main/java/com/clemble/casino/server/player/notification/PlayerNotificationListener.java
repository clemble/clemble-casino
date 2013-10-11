package com.clemble.casino.server.player.notification;

import com.clemble.casino.event.Event;

public interface PlayerNotificationListener<T extends Event> {

    public void onUpdate(String player, T event);

}
