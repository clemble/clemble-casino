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
public class ShortGoalLostOutcomeAspectFactory implements ShortGoalAspectFactory<GoalEndedEvent> {

    final private SystemNotificationService systemNotificationService;

    public ShortGoalLostOutcomeAspectFactory(SystemNotificationService systemNotificationService) {
        this.systemNotificationService = systemNotificationService;
    }

    @Override
    public ClembleAspect<GoalEndedEvent> construct(GoalConfiguration configuration, GoalState context) {
        // Step 1. Checking player bid
        return new ShortGoalLostOutcomeAspect(context.getBank().getBids(), systemNotificationService);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 2;
    }

}
