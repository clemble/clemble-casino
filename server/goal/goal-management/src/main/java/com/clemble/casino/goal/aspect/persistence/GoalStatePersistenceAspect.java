package com.clemble.casino.goal.aspect.persistence;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.repository.GoalStateRepository;

/**
 * Created by mavarazy on 14/10/14.
 */
public class GoalStatePersistenceAspect extends GoalAspect<GoalEvent>{

    final private GoalState state;
    final private GoalStateRepository stateRepository;

    public GoalStatePersistenceAspect(GoalState state, GoalStateRepository stateRepository) {
        super(new EventTypeSelector(GoalEvent.class));
        this.state = state;
        this.stateRepository = stateRepository;
    }

    @Override
    public void doEvent(GoalEvent event) {
        stateRepository.save(state);
    }
}
