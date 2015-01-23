package com.clemble.casino.goal.aspect.persistence;

import com.clemble.casino.goal.aspect.ShortGoalAspectFactory;
import com.clemble.casino.goal.event.GoalEvent;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.goal.repository.GoalStateRepository;
import com.clemble.casino.server.aspect.ClembleAspect;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 14/10/14.
 */
public class GoalStatePersistenceAspectFactory implements ShortGoalAspectFactory<GoalManagementEvent> {

    final private GoalStatePersistenceAspect persistenceAspect;

    public GoalStatePersistenceAspectFactory(GoalStateRepository stateRepository) {
        this.persistenceAspect = new GoalStatePersistenceAspect(stateRepository);
    }

    @Override
    public ClembleAspect<GoalManagementEvent> construct(GoalConfiguration configuration, GoalState context) {
        return persistenceAspect;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 4;
    }

}
