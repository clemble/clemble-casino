package com.clemble.casino.server.player.notification;

import com.clemble.casino.server.event.SystemEvent;

public interface SystemEventListener<T extends SystemEvent>{

    public void onEvent(String channel, T event);

}
