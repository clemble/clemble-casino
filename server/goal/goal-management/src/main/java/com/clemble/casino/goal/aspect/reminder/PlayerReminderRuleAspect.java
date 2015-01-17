package com.clemble.casino.goal.aspect.reminder;

import static com.clemble.casino.client.event.EventSelectors.*;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.BasicReminderRule;
import com.clemble.casino.goal.lifecycle.management.event.*;
import com.clemble.casino.goal.service.ReminderService;
import com.google.common.collect.ImmutableMap;
import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 12/10/14.
 */
public class PlayerReminderRuleAspect extends GoalAspect<GoalManagementEvent> {

    final private long hoursToReminder;
    final private BasicReminderRule reminderRule;
    final private ReminderService reminderService;

    public PlayerReminderRuleAspect(BasicReminderRule reminderRule, ReminderService reminderService) {
        super(
            where(new EventTypeSelector(GoalManagementEvent.class)).
            and(not(new EventTypeSelector(GoalChangedBetEvent.class)))
        );
        this.reminderRule = reminderRule;
        this.reminderService = reminderService;
        this.hoursToReminder = TimeUnit.MILLISECONDS.toHours(reminderRule.getReminder());
    }

    @Override
    protected void doEvent(GoalManagementEvent event) {
        // Step 1. Generating goal
        String goal = event.getBody().getGoal();
        // Step 2. Generating reminder dates
        long breachTime = event.getBody().getContext().getPlayerContexts().get(0).getClock().getBreachTime();
        if (event instanceof GoalEndedEvent) {
            reminderService.cancelReminder(event.getBody().getPlayer(), event.getBody().getGoalKey());
        } else {
            // Step 2.1. Generating remind time
            long remindTime = breachTime - reminderRule.getReminder();
            // Step 2.2. Scheduling reminder
            reminderService.scheduleReminder(
                event.getBody().getPlayer(),
                event.getBody().getGoalKey(),
                "goal_due",
                ImmutableMap.<String, String>of("text", hoursToReminder + " hours to " + goal),
                new DateTime(remindTime)
            );
        }
    }

}
