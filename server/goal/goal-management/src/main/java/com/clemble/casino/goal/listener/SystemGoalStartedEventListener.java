package com.clemble.casino.goal.listener;

import com.clemble.casino.game.lifecycle.initiation.event.GameInitiationCompleteEvent;
import com.clemble.casino.goal.action.GoalManagerFactoryFacade;
import com.clemble.casino.goal.lifecycle.initiation.event.GoalInitiationCompleteEvent;
import com.clemble.casino.server.event.goal.SystemGoalStartedEvent;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.SystemEventListener;

/**
 * Created by mavarazy on 9/20/14.
 */
public class SystemGoalStartedEventListener implements SystemEventListener<SystemGoalStartedEvent> {

    final private PlayerNotificationService notificationService;
    final private GoalManagerFactoryFacade managerFactory;

    public SystemGoalStartedEventListener(
        PlayerNotificationService notificationService,
        GoalManagerFactoryFacade managerFactory) {
        this.managerFactory = managerFactory;
        this.notificationService = notificationService;
    }

    @Override
    public void onEvent(SystemGoalStartedEvent event) {
        // Step 1. Send notification to player
        notificationService.send(event.getInitiation().getConfiguration().getPrivacyRule(), new GoalInitiationCompleteEvent(event.getInitiation().getPlayer(), event.getGoalKey()));
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
