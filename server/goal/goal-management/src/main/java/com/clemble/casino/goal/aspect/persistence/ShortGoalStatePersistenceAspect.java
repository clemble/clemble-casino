package com.clemble.casino.goal.aspect.persistence;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.aspect.ShortGoalAspect;
import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.ShortGoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.goal.repository.ShortGoalStateRepository;

/**
 * Created by mavarazy on 14/10/14.
 */
public class ShortGoalStatePersistenceAspect extends ShortGoalAspect<GoalEvent> {

    final private ShortGoalState state;
    final private ShortGoalStateRepository stateRepository;

    public ShortGoalStatePersistenceAspect(ShortGoalState state, ShortGoalStateRepository stateRepository) {
        super(new EventTypeSelector(GoalEvent.class));
        this.state = state;
        this.stateRepository = stateRepository;
    }

    @Override
    protected void doEvent(GoalEvent event) {
        if(event instanceof GoalEndedEvent) {
            stateRepository.delete(event.getBody().getGoalKey());
        } else {
            stateRepository.save(state);
        }
    }
}
