package com.clemble.casino.goal.aspect.time;

import com.clemble.casino.event.Event;
import com.clemble.casino.goal.aspect.GoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.executor.EventTask;
import com.clemble.casino.server.executor.EventTaskExecutor;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Sort;

/**
 * Created by mavarazy on 10/9/14.
 */
public class GoalTimeAspectFactory implements GoalAspectFactory<Event> {

    final private EventTaskExecutor taskExecutor;

    public GoalTimeAspectFactory(EventTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public ClembleAspect<Event> construct(GoalConfiguration configuration, GoalState state) {
        return new GoalTimeAspect(state, taskExecutor);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
