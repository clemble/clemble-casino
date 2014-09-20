package com.clemble.casino.goal.lisetener;

import com.clemble.casino.goal.action.GoalManagerFactory;
import com.clemble.casino.server.event.goal.SystemGoalStartedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;

/**
 * Created by mavarazy on 9/20/14.
 */
public class SystemGoalStartedEventListener implements SystemEventListener<SystemGoalStartedEvent> {

    final private GoalManagerFactory managerFactory;

    public SystemGoalStartedEventListener(GoalManagerFactory managerFactory) {
        this.managerFactory = managerFactory;
    }

    @Override
    public void onEvent(SystemGoalStartedEvent event) {
        managerFactory.start(event.getInitiation());
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
