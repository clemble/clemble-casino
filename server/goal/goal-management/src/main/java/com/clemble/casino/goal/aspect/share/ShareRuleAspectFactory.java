package com.clemble.casino.goal.aspect.share;

import com.clemble.casino.goal.aspect.GenericGoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.social.SocialProvider;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 1/10/15.
 */
public class ShareRuleAspectFactory implements GenericGoalAspectFactory<GoalManagementEvent> {

    final private SocialProvider provider;
    final private ShareRuleAspect shareRule;
    final private int order;

    public ShareRuleAspectFactory(SocialProvider provider, SystemNotificationService notificationService, int order) {
        this.provider = provider;
        this.shareRule = new ShareRuleAspect(provider, notificationService);
        this.order = order;
    }

    @Override
    public ClembleAspect<GoalManagementEvent> construct(GoalConfiguration configuration, GoalState state) {
        // Step 1. If share is none ignore
        if(configuration.getShareRule().isSet(provider))
            return shareRule;
        // Step 2. Checking Share rule
        return null;
    }

    @Override
    public int getOrder() {
        return order;
    }

}
