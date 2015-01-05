package com.clemble.casino.goal.aspect.timeout;

import com.clemble.casino.goal.aspect.ShortGoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 1/5/15.
 */
public class GoalTimeoutAspectFactory implements ShortGoalAspectFactory<GoalManagementEvent> {

    final private SystemNotificationService notificationService;

    public GoalTimeoutAspectFactory(SystemNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public ClembleAspect<GoalManagementEvent> construct(GoalConfiguration configuration, GoalState state) {
        return new GoalTimeoutAspect(configuration.getMoveTimeoutRule(), configuration.getTotalTimeoutRule(), notificationService);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
