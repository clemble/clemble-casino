package com.clemble.casino.goal.aspect.notification;

import com.clemble.casino.goal.aspect.GoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 11/16/14.
 */
public class GoalPlayerNotificationAspectFactory implements GoalAspectFactory<GoalManagementEvent> {

    final private PlayerNotificationService notificationService;

    public GoalPlayerNotificationAspectFactory(PlayerNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public ClembleAspect<GoalManagementEvent> construct(GoalConfiguration configuration, GoalState state) {
        // TODO extend support in future
        return new GoalPlayerPrivateNotificationService(state, notificationService);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }
}
