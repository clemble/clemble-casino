package com.clemble.casino.goal.aspect.outcome;

import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.aspect.GoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalMissedEvent;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 10/9/14.
 */
public class GoalMissedOutcomeAspectFactory implements GoalAspectFactory<GoalMissedEvent>{

    final private SystemNotificationService systemNotificationService;

    public GoalMissedOutcomeAspectFactory(SystemNotificationService systemNotificationService) {
        this.systemNotificationService = systemNotificationService;
    }

    @Override
    public ClembleAspect<GoalMissedEvent> construct(GoalConfiguration configuration, GoalState context) {
        return new GoalMissedOutcomeAspect(context.getPlayer(), configuration.getBid().getAmount(), systemNotificationService);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

}
