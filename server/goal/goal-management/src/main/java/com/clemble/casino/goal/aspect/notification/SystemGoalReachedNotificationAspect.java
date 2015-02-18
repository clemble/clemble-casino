package com.clemble.casino.goal.aspect.notification;

import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.event.OutcomeTypeSelector;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.lifecycle.management.outcome.PlayerWonOutcome;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.event.goal.SystemGoalReachedEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;

/**
 * Created by mavarazy on 2/3/15.
 */
public class SystemGoalReachedNotificationAspect extends ClembleAspect<GoalEndedEvent> {

    final private SystemNotificationService notificationService;

    public SystemGoalReachedNotificationAspect(SystemNotificationService systemNotificationService) {
        super(EventSelectors.
                where(new EventTypeSelector(GoalEndedEvent.class)).
                and(new OutcomeTypeSelector(PlayerWonOutcome.class)));
        this.notificationService = systemNotificationService;
    }

    @Override
    protected void doEvent(GoalEndedEvent event) {
        // Step 1. Extracting state
        GoalState state = event.getBody();
        // Step 2. Publishing goal reached aspect
        notificationService.send(new SystemGoalReachedEvent(state.getGoalKey(), state.getPlayer(), state.getSupporters(), state.getGoal(), state.getTag()));
    }

}
