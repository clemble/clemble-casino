package com.clemble.casino.server.player.presence;

import java.util.Collection;

import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;

public interface SystemNotificationServiceListener {

    public void subscribe(String channel, SystemEventListener<? extends SystemEvent> messageListener);

    public void subscribe(Collection<String> channels, SystemEventListener<? extends SystemEvent> messageListener);

    public void subscribe(EventSelector eventSelector, SystemEventListener<? extends SystemEvent> messageListener);

    public void unsubscribe(String channel, SystemEventListener<? extends SystemEvent> messageListener);

    public void unsubscribe(Collection<String> channels, SystemEventListener<? extends SystemEvent> playerStateListener);

    public void unsubscribe(EventSelector eventSelector, SystemEventListener<? extends SystemEvent> playerStateListener);

}
