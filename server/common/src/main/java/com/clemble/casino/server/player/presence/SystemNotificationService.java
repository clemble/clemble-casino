package com.clemble.casino.server.player.presence;

import com.clemble.casino.event.Event;
import com.clemble.casino.server.NotificationService;

public interface SystemNotificationService extends NotificationService {

    public void notify(String channel, Event event);

}
