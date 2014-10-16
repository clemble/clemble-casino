package com.clemble.casino.goal.aspect.persistence;

import com.clemble.casino.goal.aspect.GoalAspectFactory;
import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.repository.GoalStateRepository;
import com.clemble.casino.server.aspect.ClembleAspect;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 14/10/14.
 */
public class GoalStatePersistenceAspectFactory implements GoalAspectFactory<GoalEvent>{

    final private GoalStateRepository stateRepository;

    public GoalStatePersistenceAspectFactory(GoalStateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public ClembleAspect<GoalEvent> construct(GoalConfiguration configuration, GoalState context) {
        return new GoalStatePersistenceAspect(context, stateRepository);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
