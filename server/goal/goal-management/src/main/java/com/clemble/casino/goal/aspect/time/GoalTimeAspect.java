package com.clemble.casino.goal.aspect.time;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.event.Event;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.management.GoalContext;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.goal.lifecycle.management.event.GoalReachedEvent;
import com.clemble.casino.lifecycle.management.PlayerContext;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.aspect.time.PlayerTimeTask;
import com.clemble.casino.server.aspect.time.PlayerTimeTracker;
import com.clemble.casino.server.executor.EventTaskExecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mavarazy on 10/9/14.
 */
public class GoalTimeAspect extends GoalAspect<Event> {

    final private GoalContext context;
    final private Collection<String> participants;
    final private PlayerTimeTask timeTracker;
    final private EventTaskExecutor eventTaskExecutor;

    public GoalTimeAspect(
            GoalState state,
            EventTaskExecutor eventTaskExecutor) {
        super(new EventTypeSelector(Event.class));
        this.context  = state.getContext();
        this.participants = PlayerAwareUtils.toPlayerList(context.getPlayerContexts());
        this.eventTaskExecutor = checkNotNull(eventTaskExecutor);

        List<PlayerTimeTracker> playerTimeTrackers = new ArrayList<PlayerTimeTracker>();
        for (PlayerContext playerContext : context.getPlayerContexts()) {
            playerTimeTrackers.add(new PlayerTimeTracker(playerContext.getPlayer(), state.getGoalKey(), playerContext.getClock(), state.getConfiguration().getTotalTimeRule()));
            playerTimeTrackers.add(new PlayerTimeTracker(playerContext.getPlayer(), state.getGoalKey(), playerContext.getClock(), state.getConfiguration().getMoveTimeRule()));
        }

        this.timeTracker = new PlayerTimeTask(state.getGoalKey(), playerTimeTrackers);
    }

    @Override
    public void doEvent(Event move) {
        // Step 1. To check if we need rescheduling, first calculate time before
        Date breachTimeBeforeMove = timeTracker.nextExecutionTime(null);
        if(move instanceof GoalEndedEvent) {
            eventTaskExecutor.cancel(timeTracker);
        } else {
            timeTracker.markMoved((PlayerAware) move);
            timeTracker.markToMove((PlayerAware) move);
            // Step 3. Re scheduling if needed
            if (!timeTracker.nextExecutionTime(null).equals(breachTimeBeforeMove)) {
                eventTaskExecutor.reschedule(timeTracker);
            }
        }
    }

}

