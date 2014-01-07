package com.clemble.casino.server.player.presence;

import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;

public interface SystemNotificationServiceListener {

    public void subscribe(SystemEventListener<? extends SystemEvent> messageListener);

    public void unsubscribe(SystemEventListener<? extends SystemEvent> messageListener);

    public void close();

}
