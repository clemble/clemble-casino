package com.clemble.casino.goal.aspect.persistence;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.goal.repository.GoalStateRepository;

/**
 * Created by mavarazy on 14/10/14.
 */
public class GoalStatePersistenceAspect extends GoalAspect<GoalManagementEvent> {

    final private GoalStateRepository stateRepository;

    public GoalStatePersistenceAspect(GoalStateRepository stateRepository) {
        super(new EventTypeSelector(GoalManagementEvent.class));
        this.stateRepository = stateRepository;
    }

    @Override
    protected void doEvent(GoalManagementEvent event) {
        if(event instanceof GoalEndedEvent) {
            stateRepository.delete(event.getBody().getGoalKey());
        } else {
            stateRepository.save(event.getBody());
        }
    }
}
