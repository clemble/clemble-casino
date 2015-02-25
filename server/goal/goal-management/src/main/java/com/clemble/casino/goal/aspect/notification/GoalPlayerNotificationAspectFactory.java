package com.clemble.casino.goal.aspect.notification;

import com.clemble.casino.goal.aspect.GenericGoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 11/16/14.
 */
public class GoalPlayerNotificationAspectFactory implements GenericGoalAspectFactory<GoalManagementEvent> {

    final private ServerNotificationService notificationService;
    final private GoalPlayerNotificationAspect aspect;

    public GoalPlayerNotificationAspectFactory(ServerNotificationService notificationService) {
        this.notificationService = notificationService;
        this.aspect = new GoalPlayerNotificationAspect(notificationService);
    }

    @Override
    public ClembleAspect<GoalManagementEvent> construct(GoalConfiguration configuration, GoalState state) {
        // Step 1. Generating list of participants
        return aspect;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
