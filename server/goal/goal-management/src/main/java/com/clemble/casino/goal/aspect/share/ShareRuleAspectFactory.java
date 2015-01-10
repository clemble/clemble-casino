package com.clemble.casino.goal.aspect.share;

import com.clemble.casino.goal.aspect.GenericGoalAspectFactory;
import com.clemble.casino.goal.aspect.GoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.rule.share.ShareRule;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.core.Ordered;

import java.util.concurrent.ExecutionException;

/**
 * Created by mavarazy on 1/10/15.
 */
public class ShareRuleAspectFactory implements GenericGoalAspectFactory<GoalManagementEvent> {

    final private SystemNotificationService notificationService;

    public ShareRuleAspectFactory(SystemNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public ClembleAspect<GoalManagementEvent> construct(GoalConfiguration configuration, GoalState state) {
        // Step 1. If share is none ignore
        if(configuration.getShareRule() == ShareRule.none)
            return null;
        // Step 2. Checking Share rule
        return new ShareRuleAspect(configuration.getShareRule(), notificationService);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 10;
    }
}
