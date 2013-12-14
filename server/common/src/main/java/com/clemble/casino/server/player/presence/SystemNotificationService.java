package com.clemble.casino.server.player.presence;

import com.clemble.casino.server.NotificationService;
import com.clemble.casino.server.event.SystemEvent;

public interface SystemNotificationService extends NotificationService {

    public void notify(String channel, SystemEvent event);

}
