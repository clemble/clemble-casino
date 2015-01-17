package com.clemble.casino.goal.aspect.outcome;

import com.clemble.casino.goal.aspect.ShortGoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 10/9/14.
 */
public class GoalWonOutcomeAspectFactory implements ShortGoalAspectFactory<GoalEndedEvent> {

    final private GoalWonOutcomeAspect INSTANCE;

    public GoalWonOutcomeAspectFactory(SystemNotificationService notificationService) {
        this.INSTANCE = new GoalWonOutcomeAspect(notificationService);
    }

    @Override
    public ClembleAspect<GoalEndedEvent> construct(GoalConfiguration configuration, GoalState context) {
        return INSTANCE;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

}
