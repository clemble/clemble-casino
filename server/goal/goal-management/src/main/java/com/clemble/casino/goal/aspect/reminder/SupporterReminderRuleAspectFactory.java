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

import java.util.function.Function;

/**
 * Created by mavarazy on 1/12/15.
 */
public class SupporterReminderRuleAspectFactory  implements GenericGoalAspectFactory<GoalManagementEvent> {

    final private ReminderService reminderService;
    final private Function<GoalConfiguration, ReminderRule> roleExtractor;
    final private int order;

    // TODO not the best solution think of something better
    public SupporterReminderRuleAspectFactory(int order, ReminderService emailReminderService, Function<GoalConfiguration, ReminderRule> roleExtractor) {
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
            return new SupporterReminderRuleAspect((BasicReminderRule) reminderRule, reminderService);
        }
    }

    @Override
    public int getOrder() {
        return order;
    }

}

