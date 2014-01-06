package com.clemble.casino.server.player.presence;

import com.clemble.casino.server.NotificationService;
import com.clemble.casino.server.event.SystemEvent;

// TODO System notifications must be Durable (For payment and bonuses).
// Redis does not provide durable notifications
public interface SystemNotificationService extends NotificationService {

    public void notify(String channel, SystemEvent event);

}
