package com.clemble.casino.goal.aspect.time;

import com.clemble.casino.event.Event;
import com.clemble.casino.goal.aspect.GoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 10/9/14.
 */
public class GoalTimeAspectFactory implements GoalAspectFactory<Event> {

    final private SystemNotificationService systemNotificationService;

    public GoalTimeAspectFactory(SystemNotificationService taskExecutor) {
        this.systemNotificationService = taskExecutor;
    }

    @Override
    public ClembleAspect<Event> construct(GoalConfiguration configuration, GoalState state) {
        return new GoalTimeAspect(state, systemNotificationService);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
