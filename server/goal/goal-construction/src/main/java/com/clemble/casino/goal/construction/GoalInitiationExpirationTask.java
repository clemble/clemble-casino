package com.clemble.casino.goal.construction;

import com.clemble.casino.event.Event;
import com.clemble.casino.server.event.game.SystemGameInitiationDueEvent;
import com.clemble.casino.server.event.goal.SystemGoalInitiationDueEvent;
import com.clemble.casino.server.event.goal.SystemGoalStartedEvent;
import com.clemble.casino.server.executor.EventTask;
import org.springframework.scheduling.TriggerContext;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * Created by mavarazy on 9/13/14.
 */
public class GoalInitiationExpirationTask implements EventTask {

    final private String goalKey;
    final private Date startDate;

    public GoalInitiationExpirationTask(String goalKey, Date startDate) {
        this.goalKey = goalKey;
        this.startDate = startDate;
    }

    @Override
    public String getKey() {
        return goalKey;
    }

    @Override
    public Collection<? extends Event> execute() {
        return Collections.singleton(new SystemGoalInitiationDueEvent(goalKey));
    }

    @Override
    public Date nextExecutionTime() {
        return startDate;
    }

}
