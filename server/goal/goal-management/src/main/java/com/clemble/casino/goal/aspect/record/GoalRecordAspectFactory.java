package com.clemble.casino.goal.aspect.record;

import com.clemble.casino.event.Event;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.aspect.GoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.repository.GoalRecordRepository;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 10/03/14.
 */
public class GoalRecordAspectFactory implements GoalAspectFactory<Event> {

    final private GoalRecordRepository recordRepository;

    public GoalRecordAspectFactory(GoalRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public GoalAspect<Event> construct(GoalConfiguration configuration, GoalState state) {
        return new GoalRecordEventAspect(state.getGoalKey(), recordRepository);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
