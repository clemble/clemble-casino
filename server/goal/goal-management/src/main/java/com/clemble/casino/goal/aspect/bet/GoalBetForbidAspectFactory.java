package com.clemble.casino.goal.aspect.bet;

import com.clemble.casino.goal.aspect.GenericGoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalStartedEvent;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 1/13/15.
 */
public class GoalBetForbidAspectFactory implements GenericGoalAspectFactory<GoalStartedEvent> {

    final private SystemNotificationService notificationService;

    public GoalBetForbidAspectFactory(SystemNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public ClembleAspect<GoalStartedEvent> construct(GoalConfiguration configuration, GoalState state) {
        return new GoalBetForbidAspect(configuration, notificationService);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 6;
    }

}
