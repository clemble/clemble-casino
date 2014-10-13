package com.clemble.casino.goal.aspect.time;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.event.Event;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.server.aspect.time.PlayerClockTimeoutEventTask;
import com.clemble.casino.server.executor.EventTaskExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mavarazy on 10/9/14.
 */
public class GoalTimeAspect extends GoalAspect<Event> {

    final private Map<String, PlayerClockTimeoutEventTask> playerToTask = new HashMap<>();

    public GoalTimeAspect(
            GoalState state,
            EventTaskExecutor eventTaskExecutor) {
        super(new EventTypeSelector(Event.class));

        state.getContext().getPlayerContexts().forEach(playerContext -> {
            PlayerClockTimeoutEventTask timeoutEventTask = new PlayerClockTimeoutEventTask(
                state.getGoalKey(),
                playerContext.getPlayer(),
                playerContext.getClock(),
                state.getConfiguration().getMoveTimeRule(),
                state.getConfiguration().getTotalTimeRule(), eventTaskExecutor);
            playerToTask.put(playerContext.getPlayer(), timeoutEventTask);
        });
    }

    @Override
    public void doEvent(Event move) {
        // Step 1. To check if we need rescheduling, first calculate time before
        if(move instanceof GoalEndedEvent) {
            playerToTask.values().forEach(task -> {
                task.stop();
            });
        } else {
            playerToTask.values().forEach(task -> {
                task.stop();
                task.start();
            });
        }
    }

}

