package com.clemble.casino.goal.aspect.notification;

import com.clemble.casino.goal.aspect.GenericGoalAspectFactory;
import com.clemble.casino.goal.aspect.GoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.lifecycle.configuration.rule.privacy.PrivacyRule;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by mavarazy on 11/16/14.
 */
public class GoalPlayerNotificationAspectFactory implements GenericGoalAspectFactory<GoalManagementEvent> {

    final private ServerNotificationService notificationService;

    public GoalPlayerNotificationAspectFactory(ServerNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public ClembleAspect<GoalManagementEvent> construct(GoalConfiguration configuration, GoalState state) {
        // Step 1. Generating list of participants
        return new GoalPlayerNotificationAspect(configuration.getPrivacyRule(), notificationService);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
