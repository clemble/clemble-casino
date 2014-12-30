package com.clemble.casino.goal.aspect.reminder;

import com.clemble.casino.goal.aspect.GenericGoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.BasicReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.NoReminderRule;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.ReminderRule;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.goal.service.ReminderService;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/**
 * Created by mavarazy on 12/12/14.
 */
public class ReminderRuleAspectFactory implements GenericGoalAspectFactory<GoalManagementEvent> {

    final private ReminderService reminderService;
    final private Function<GoalConfiguration, ReminderRule> roleExtractor;
    final private int order;

    // TODO not the best solution think of something better
    public ReminderRuleAspectFactory(int order, ReminderService emailReminderService, Function<GoalConfiguration, ReminderRule> roleExtractor) {
        this.order = order;
        this.roleExtractor = roleExtractor;
        this.reminderService = emailReminderService;
    }

    @Override
    public ClembleAspect<GoalManagementEvent> construct(GoalConfiguration configuration, GoalState state) {
        ReminderRule reminderRule = roleExtractor.apply(configuration);
        if (reminderRule == null || reminderRule instanceof NoReminderRule) {
            return null;
        } else {
            return new ReminderRuleAspect(state.getPlayer(), (BasicReminderRule)reminderRule, reminderService);
        }
    }

    @Override
    public int getOrder() {
        return order;
    }

}
