package com.clemble.casino.goal.aspect.notification;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by mavarazy on 11/16/14.
 */
public class GoalPlayerPrivateNotificationService extends GoalAspect<GoalManagementEvent> {

    final private Collection<String> participants;
    final private PlayerNotificationService notificationService;

    public GoalPlayerPrivateNotificationService(GoalState state, PlayerNotificationService notificationService){
        super(new EventTypeSelector(GoalManagementEvent.class));
        this.participants = Collections.singleton(state.getPlayer());
        this.notificationService = notificationService;
    }

    @Override
    protected void doEvent(GoalManagementEvent event) {
        notificationService.send(participants, event);
    }

}
