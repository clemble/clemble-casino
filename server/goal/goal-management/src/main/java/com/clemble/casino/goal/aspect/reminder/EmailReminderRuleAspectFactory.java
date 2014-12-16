package com.clemble.casino.goal.aspect.reminder;

import com.clemble.casino.goal.aspect.GenericGoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.BasicReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.NoReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.ReminderRule;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.goal.service.EmailReminderService;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.core.Ordered;

import java.util.concurrent.ExecutionException;

/**
 * Created by mavarazy on 12/12/14.
 */
public class EmailReminderRuleAspectFactory implements GenericGoalAspectFactory<GoalManagementEvent> {

    final private EmailReminderService emailReminderService;
    final private LoadingCache<BasicReminderRule, EmailReminderRuleAspect> CACHE = CacheBuilder.
        newBuilder().
        build(
            new CacheLoader<BasicReminderRule, EmailReminderRuleAspect>() {
                @Override
                public EmailReminderRuleAspect load(BasicReminderRule reminderRule) {
                    return new EmailReminderRuleAspect(reminderRule, emailReminderService);
                }
            }
        );

    public EmailReminderRuleAspectFactory(EmailReminderService emailReminderService) {
        this.emailReminderService = emailReminderService;
    }

    @Override
    public ClembleAspect<GoalManagementEvent> construct(GoalConfiguration configuration, GoalState state) {
        try {
            ReminderRule emailReminderRule = configuration.getRoleConfigurations().get(0).getEmailReminderRule();
            if (emailReminderRule instanceof NoReminderRule) {
                return null;
            } else {
                return CACHE.get((BasicReminderRule) emailReminderRule);
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 4;
    }
}
