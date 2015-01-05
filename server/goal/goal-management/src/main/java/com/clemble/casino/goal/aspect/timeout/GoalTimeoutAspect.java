package com.clemble.casino.goal.aspect.timeout;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.management.GoalContext;
import com.clemble.casino.goal.lifecycle.management.event.GoalChangedEvent;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.goal.lifecycle.management.event.GoalStartedEvent;
import com.clemble.casino.lifecycle.configuration.rule.breach.BreachPunishment;
import com.clemble.casino.lifecycle.configuration.rule.timeout.TimeoutRule;
import com.clemble.casino.lifecycle.management.PlayerContext;
import com.clemble.casino.server.event.goal.SystemGoalTimeoutEvent;
import com.clemble.casino.server.event.schedule.SystemAddJobScheduleEvent;
import com.clemble.casino.server.event.schedule.SystemRemoveJobScheduleEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;

import java.util.Date;

/**
 * Created by mavarazy on 1/4/15.
 */
public class GoalTimeoutAspect extends GoalAspect<GoalManagementEvent>{

    final private TimeoutRule moveTimeoutRule;
    final private TimeoutRule totalTimeoutRule;
    final private SystemNotificationService notificationService;

    public GoalTimeoutAspect(TimeoutRule moveTimeoutRule, TimeoutRule totalTimeoutRule, SystemNotificationService notificationService) {
        super(new EventTypeSelector(GoalManagementEvent.class));
        this.moveTimeoutRule = moveTimeoutRule;
        this.totalTimeoutRule = totalTimeoutRule;
        this.notificationService = notificationService;
    }

    @Override
    protected void doEvent(GoalManagementEvent event) {
        // Step 1. Preparing for processing
        String goalKey = event.getBody().getGoalKey();
        GoalContext context = event.getBody().getContext();
        // Step 2. Process depending on event
        if (event instanceof GoalStartedEvent) {
            // Case 1. Goal just started
            context.getPlayerContexts().forEach((c) -> {
                long startTime = System.currentTimeMillis() + 5_000;
                long deadline = totalTimeoutRule.getTimeoutCalculator().calculate(startTime);
                long breachTime = moveTimeoutRule.getTimeoutCalculator().calculate(startTime);
                BreachPunishment punishment = null;
                if (breachTime < deadline) {
                    punishment = moveTimeoutRule.getPunishment();
                } else {
                    punishment = totalTimeoutRule.getPunishment();
                    breachTime = deadline;
                }
                c.getClock().start(startTime, breachTime, deadline, punishment);
                notificationService.send(new SystemAddJobScheduleEvent(goalKey, toKey(c.getPlayer()), new SystemGoalTimeoutEvent(goalKey), new Date(breachTime)));
            });
        } else if (event instanceof GoalEndedEvent) {
            // Case 2. Goal ended
            context.getPlayerContexts().forEach((c) -> {
                c.getClock().stop();
                notificationService.send(new SystemRemoveJobScheduleEvent(goalKey, toKey(c.getPlayer())));
            });
        } else if (event instanceof GoalChangedEvent) {
            // Case 3. Goal changed
            context.getPlayerContexts().forEach((c) -> {
                c.getClock().stop();
                long startTime = System.currentTimeMillis() + 5_000;
                long deadline = totalTimeoutRule.getTimeoutCalculator().calculate(startTime, c.getClock().getTimeSpent());
                long breachTime = moveTimeoutRule.getTimeoutCalculator().calculate(startTime, c.getClock().getTimeSpent());
                BreachPunishment punishment = null;
                if (breachTime < deadline) {
                    punishment = moveTimeoutRule.getPunishment();
                } else {
                    punishment = totalTimeoutRule.getPunishment();
                    breachTime = deadline;
                }
                c.getClock().start(startTime, breachTime, deadline, punishment);
                notificationService.send(new SystemAddJobScheduleEvent(goalKey, toKey(c.getPlayer()), new SystemGoalTimeoutEvent(goalKey), new Date(breachTime)));
            });
        }
    }

    private String toKey(String player) {
        return "timeout:" + player;
    }
}
