package com.clemble.casino.server.player.presence;

import java.util.Collection;

import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.player.notification.SystemNotificationListener;

public interface SystemNotificationServiceListener {

    public void subscribe(String channel, SystemNotificationListener<? extends SystemEvent> messageListener);

    public void subscribe(Collection<String> channels, SystemNotificationListener<? extends SystemEvent> messageListener);

    public void subscribe(EventSelector eventSelector, SystemNotificationListener<? extends SystemEvent> messageListener);

    public void unsubscribe(String channel, SystemNotificationListener<? extends SystemEvent> messageListener);

    public void unsubscribe(Collection<String> channels, SystemNotificationListener<? extends SystemEvent> playerStateListener);

    public void unsubscribe(EventSelector eventSelector, SystemNotificationListener<? extends SystemEvent> playerStateListener);

}
