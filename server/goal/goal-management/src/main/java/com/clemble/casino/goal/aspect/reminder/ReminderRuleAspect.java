package com.clemble.casino.goal.aspect.reminder;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.configuration.rule.reminder.BasicReminderRule;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.goal.service.ReminderService;
import com.clemble.casino.lifecycle.management.PlayerContext;
import com.clemble.casino.player.PlayerAware;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 12/10/14.
 */
public class ReminderRuleAspect extends GoalAspect<GoalManagementEvent> implements PlayerAware {

    final private String player;
    final private long hoursToReminder;
    final private BasicReminderRule reminderRule;
    final private ReminderService reminderService;

    public ReminderRuleAspect(String player, BasicReminderRule reminderRule, ReminderService reminderService) {
        super(new EventTypeSelector(GoalManagementEvent.class));
        this.player = player;
        this.reminderRule = reminderRule;
        this.reminderService = reminderService;
        this.hoursToReminder = TimeUnit.MILLISECONDS.toHours(reminderRule.getReminder());
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    protected void doEvent(GoalManagementEvent event) {
        // Step 1. Generating goal
        String goal = event.getBody().getGoal();
        // Step 2. Generating reminder dates
        long breachTime = event.getBody().getContext().getPlayerContexts().get(0).getClock().getBreachTime();
        if (event instanceof GoalEndedEvent) {
            reminderService.cancelReminder(player, event.getBody().getGoalKey());
        } else {
            // Step 2.1. Generating remind time
            long remindTime = breachTime - reminderRule.getReminder();
            // Step 2.2. Scheduling reminder
            reminderService.scheduleReminder(
                player,
                event.getBody().getGoalKey(),
                hoursToReminder + " hours to " + goal,
                new Date(remindTime)
            );
        }
    }

}
