package com.clemble.casino.goal.aspect.notification;

import com.clemble.casino.goal.aspect.GenericGoalAspectFactory;
import com.clemble.casino.goal.aspect.outcome.GoalWonOutcomeAspect;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.aspect.ClembleAspectFactory;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 2/3/15.
 */
public class SystemGoalReachedNotificationAspectFactory implements GenericGoalAspectFactory<GoalEndedEvent> {

    final private SystemGoalReachedNotificationAspect INSTANCE;

    public SystemGoalReachedNotificationAspectFactory(SystemNotificationService notificationService) {
        this.INSTANCE = new SystemGoalReachedNotificationAspect(notificationService);
    }

    @Override
    public ClembleAspect<GoalEndedEvent> construct(GoalConfiguration configuration, GoalState context) {
        return INSTANCE;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE + 4;
    }

}
