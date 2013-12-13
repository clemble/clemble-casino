package com.clemble.casino.server.player.notification;

import com.clemble.casino.server.event.SystemEvent;

public interface SystemNotificationListener<T extends SystemEvent> {

    public void onUpdate(String channel, T event);

}
