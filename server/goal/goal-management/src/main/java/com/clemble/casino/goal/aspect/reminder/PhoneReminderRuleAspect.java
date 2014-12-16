package com.clemble.casino.goal.aspect.reminder;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.BasicReminderRule;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.goal.service.PhoneReminderService;
import com.clemble.casino.lifecycle.management.PlayerContext;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 12/12/14.
 */
public class PhoneReminderRuleAspect extends GoalAspect<GoalManagementEvent> {

    final private long hoursToReminder;
    final private BasicReminderRule reminderRule;
    final private PhoneReminderService reminderService;

    public PhoneReminderRuleAspect(BasicReminderRule reminderRule, PhoneReminderService reminderService) {
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
        for (PlayerContext playerContext : event.getBody().getContext().getPlayerContexts()) {
            // Step 2.1. Generating remind time
            if (event instanceof GoalEndedEvent) {
                reminderService.cancelReminder(playerContext.getPlayer(), event.getBody().getGoalKey());
            } else {
                long remindTime = playerContext.getClock().getBreachTime() - reminderRule.getReminder();
                // Step 2.2.
                reminderService.scheduleReminder(
                    playerContext.getPlayer(),
                    event.getBody().getGoalKey(),
                    hoursToReminder + " hours to " + goal,
                    new Date(remindTime));
            }
        }
    }

}
