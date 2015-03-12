package com.clemble.casino.goal.listener;

import com.clemble.casino.goal.action.GoalManagerFactoryFacade;
import com.clemble.casino.goal.lifecycle.initiation.event.GoalInitiationCompletedEvent;
import com.clemble.casino.server.event.goal.SystemGoalStartedEvent;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemEventListener;

/**
 * Created by mavarazy on 9/20/14.
 */
public class SystemGoalStartedEventListener implements SystemEventListener<SystemGoalStartedEvent> {

    final private ServerNotificationService notificationService;
    final private GoalManagerFactoryFacade managerFactory;

    public SystemGoalStartedEventListener(
        ServerNotificationService notificationService,
        GoalManagerFactoryFacade managerFactory) {
        this.managerFactory = managerFactory;
        this.notificationService = notificationService;
    }

    @Override
    public void onEvent(SystemGoalStartedEvent event) {
        // Step 0. Checking goal key was not prior created
        if (managerFactory.get(event.getGoalKey()) != null)
            return;
        // Step 1. Send notification to player
        notificationService.send(GoalInitiationCompletedEvent.create(event.getInitiation()));
        // Step 2. Start manager for the goal
        managerFactory.start(null, event.getInitiation());
    }

    @Override
    public String getChannel() {
        return SystemGoalStartedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemGoalStartedEvent.CHANNEL + "> goal:management";
    }
}
