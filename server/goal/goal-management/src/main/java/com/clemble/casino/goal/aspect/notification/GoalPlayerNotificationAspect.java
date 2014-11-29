package com.clemble.casino.goal.aspect.notification;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.lifecycle.configuration.rule.privacy.PrivacyRule;
import com.clemble.casino.lifecycle.configuration.rule.privacy.PrivacyRuleAware;
import com.clemble.casino.server.player.notification.ServerNotificationService;

import java.util.Collection;

/**
 * Created by mavarazy on 11/16/14.
 */
public class GoalPlayerNotificationAspect extends GoalAspect<GoalManagementEvent> implements PrivacyRuleAware {

    final private PrivacyRule privacyRule;
    final private ServerNotificationService notificationService;

    public GoalPlayerNotificationAspect(PrivacyRule privacyRule, ServerNotificationService notificationService){
        super(new EventTypeSelector(GoalManagementEvent.class));
        this.privacyRule = privacyRule;
        this.notificationService = notificationService;
    }

    @Override
    public PrivacyRule getPrivacyRule() {
        return privacyRule;
    }

    @Override
    protected void doEvent(GoalManagementEvent event) {
        notificationService.send(privacyRule, event);
    }

}
