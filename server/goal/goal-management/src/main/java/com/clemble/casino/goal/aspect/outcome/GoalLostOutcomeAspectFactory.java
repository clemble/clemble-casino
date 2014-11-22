package com.clemble.casino.goal.aspect.outcome;

import com.clemble.casino.bet.PlayerBid;
import com.clemble.casino.goal.aspect.GoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by mavarazy on 10/9/14.
 */
public class GoalLostOutcomeAspectFactory implements GoalAspectFactory<GoalEndedEvent>{

    final private SystemNotificationService systemNotificationService;

    public GoalLostOutcomeAspectFactory(SystemNotificationService systemNotificationService) {
        this.systemNotificationService = systemNotificationService;
    }

    @Override
    public ClembleAspect<GoalEndedEvent> construct(GoalConfiguration configuration, GoalState context) {
        // Step 1. Checking player bid
        return new GoalLostOutcomeAspect(context.getBank().getBids(), systemNotificationService);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 2;
    }

}
