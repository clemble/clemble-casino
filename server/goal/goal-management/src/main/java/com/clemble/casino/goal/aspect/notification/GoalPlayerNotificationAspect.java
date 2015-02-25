package com.clemble.casino.goal.aspect.notification;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.server.player.notification.ServerNotificationService;

/**
 * Created by mavarazy on 11/16/14.
 */
public class GoalPlayerNotificationAspect extends GoalAspect<GoalManagementEvent> {

    final private ServerNotificationService notificationService;

    public GoalPlayerNotificationAspect(ServerNotificationService notificationService){
        super(new EventTypeSelector(GoalManagementEvent.class));
        this.notificationService = notificationService;
    }

    @Override
    protected void doEvent(GoalManagementEvent event) {
        notificationService.send(event);
    }

}
