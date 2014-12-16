package com.clemble.casino.goal.aspect.reminder;

import com.clemble.casino.goal.aspect.GenericGoalAspectFactory;
import com.clemble.casino.goal.aspect.GoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.PhoneReminderRule;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.goal.service.PhoneReminderService;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.core.Ordered;

import java.util.concurrent.ExecutionException;

/**
 * Created by mavarazy on 12/12/14.
 */
public class PhoneReminderRuleAspectFactory implements GenericGoalAspectFactory<GoalManagementEvent> {

    final private PhoneReminderService reminderService;
    final private LoadingCache<PhoneReminderRule, PhoneReminderRuleAspect> CACHE = CacheBuilder.
        newBuilder().
        build(
            new CacheLoader<PhoneReminderRule, PhoneReminderRuleAspect>() {
                @Override
                public PhoneReminderRuleAspect load(PhoneReminderRule reminderRule) {
                    return new PhoneReminderRuleAspect(reminderRule, reminderService);
                }
            }
        );

    public PhoneReminderRuleAspectFactory(PhoneReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @Override
    public ClembleAspect<GoalManagementEvent> construct(GoalConfiguration configuration, GoalState state) {
        try {
            PhoneReminderRule phoneReminderRule = configuration.getRoleConfigurations().get(0).getPhoneReminderRule();
            return CACHE.get(phoneReminderRule);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 3;
    }

}

