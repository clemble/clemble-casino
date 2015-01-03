package com.clemble.casino.goal.construction.listener;

import com.clemble.casino.goal.construction.service.ServerGoalInitiationService;
import com.clemble.casino.server.event.goal.SystemGoalInitiationStartedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;

/**
 * Created by mavarazy on 1/3/15.
 */
public class SystemGoalInitiationStartedEventListener implements SystemEventListener<SystemGoalInitiationStartedEvent> {

    final private ServerGoalInitiationService initiationService;

    public SystemGoalInitiationStartedEventListener(ServerGoalInitiationService initiationService) {
        this.initiationService = initiationService;
    }

    @Override
    public void onEvent(SystemGoalInitiationStartedEvent event) {
        initiationService.start(event.getInitiation());
    }

    @Override
    public String getChannel() {
        return SystemGoalInitiationStartedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemGoalInitiationStartedEvent.CHANNEL + " > goal:initiation";
    }
}
