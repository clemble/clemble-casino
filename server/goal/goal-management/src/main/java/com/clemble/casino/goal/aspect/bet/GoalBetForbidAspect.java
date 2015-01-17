package com.clemble.casino.goal.aspect.bet;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalStartedEvent;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.event.goal.SystemGoalForbidBetEvent;
import com.clemble.casino.server.event.schedule.SystemAddJobScheduleEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;

/**
 * Created by mavarazy on 1/13/15.
 */
public class GoalBetForbidAspect extends GoalAspect<GoalStartedEvent> {

    final private int betDays;
    final private SystemNotificationService notificationService;

    public GoalBetForbidAspect(GoalConfiguration configuration, SystemNotificationService notificationService) {
        super(new EventTypeSelector(GoalStartedEvent.class));
        this.notificationService = notificationService;
        this.betDays = configuration.getSupporterConfiguration().getBetDays();
    }

    @Override
    protected void doEvent(GoalStartedEvent event) {
        GoalState state = event.getBody();
        // Step 1. Generating shedule event with bet forbid action
        SystemEvent sysEvent = new SystemAddJobScheduleEvent(state.getGoalKey(), "forbid", new SystemGoalForbidBetEvent(state.getGoalKey()), state.getStartDate().plusDays(betDays));
        // Step 3. Scheduling trigger
        notificationService.send(sysEvent);
    }

}
