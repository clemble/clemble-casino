package com.clemble.casino.goal.construction.listener;

import com.clemble.casino.lifecycle.initiation.InitiationState;
import com.clemble.casino.goal.lifecycle.initiation.GoalInitiation;
import com.clemble.casino.goal.construction.repository.GoalInitiationRepository;
import com.clemble.casino.server.event.goal.SystemGoalInitiationDueEvent;
import com.clemble.casino.server.event.goal.SystemGoalStartedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.notification.SystemNotificationService;

/**
 * Created by mavarazy on 9/19/14.
 */
public class SystemGoalInitiationExpirationEventListener implements SystemEventListener<SystemGoalInitiationDueEvent>{

    final private GoalInitiationRepository initiationRepository;
    final private SystemNotificationService notificationService;

    public SystemGoalInitiationExpirationEventListener(SystemNotificationService notificationService, GoalInitiationRepository initiationRepository) {
        this.notificationService = notificationService;
        this.initiationRepository = initiationRepository;
    }

    @Override
    public void onEvent(SystemGoalInitiationDueEvent event) {
        // Step 1. Fetching related initiation
        GoalInitiation initiation = initiationRepository.findOne(event.getGoalKey());
        // Step 1.1 Updating initiation state
        initiation = initiation.copyWithState(InitiationState.initiated);
        // Step 1.2 Saving new initiation
        initiationRepository.save(initiation);
        // Step 2. Notifying of goal started event, for manager processing
        notificationService.notify(new SystemGoalStartedEvent(initiation.getGoalKey(), initiation));
    }

    @Override
    public String getChannel() {
        return SystemGoalInitiationDueEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemGoalInitiationDueEvent.CHANNEL + " > goal:initiation";
    }
}
