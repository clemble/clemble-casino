package com.clemble.casino.goal.aspect.time;

import com.clemble.casino.event.Event;
import com.clemble.casino.goal.aspect.ShortGoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 10/9/14.
 */
public class ShortGoalTimeAspectFactory implements ShortGoalAspectFactory<Event> {

    final private SystemNotificationService systemNotificationService;

    public ShortGoalTimeAspectFactory(SystemNotificationService taskExecutor) {
        this.systemNotificationService = taskExecutor;
    }

    @Override
    public ClembleAspect<Event> construct(GoalConfiguration configuration, GoalState state) {
        return new ShortGoalTimeAspect(state, systemNotificationService);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
