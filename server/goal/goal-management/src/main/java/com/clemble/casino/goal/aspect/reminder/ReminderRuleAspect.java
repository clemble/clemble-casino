package com.clemble.casino.goal.aspect.reminder;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.BasicReminderRule;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.goal.service.ReminderService;
import com.clemble.casino.lifecycle.management.PlayerContext;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 12/10/14.
 */
public class ReminderRuleAspect extends GoalAspect<GoalManagementEvent>{

    final private long hoursToReminder;
    final private BasicReminderRule reminderRule;
    final private ReminderService reminderService;

    public ReminderRuleAspect(BasicReminderRule reminderRule, ReminderService reminderService) {
        super(new EventTypeSelector(GoalManagementEvent.class));
        this.reminderRule = reminderRule;
        this.reminderService = reminderService;
        this.hoursToReminder = TimeUnit.MILLISECONDS.toHours(reminderRule.getReminder());
    }


    @Override
    protected void doEvent(GoalManagementEvent event) {
        // Step 1. Generating goal
        String goal = event.getBody().getGoal();
        // Step 2. Generating reminder dates
        for (PlayerContext playerContext: event.getBody().getContext().getPlayerContexts()) {
            if (event instanceof GoalEndedEvent) {
                reminderService.cancelReminder(playerContext.getPlayer(), event.getBody().getGoalKey());
            } else {
                // Step 2.1. Generating remind time
                long remindTime = playerContext.getClock().getBreachTime() - reminderRule.getReminder();
                // Step 2.2. Scheduling reminder
                reminderService.scheduleReminder(
                    playerContext.getPlayer(),
                    event.getBody().getGoalKey(),
                    hoursToReminder + " hours to " + goal,
                    new Date(remindTime));
            }
        }
    }
}