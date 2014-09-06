package com.clemble.casino.server.game.action;

import com.clemble.casino.event.Event;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameEventTaskExecutor {

    final private GameManagerFactory sessionProcessor;
    final private ConcurrentHashMap<GameEventTask, ScheduledFuture<?>> concurrentHashMap = new ConcurrentHashMap<>();

    final private ScheduledExecutorService scheduledExecutorService;

    public GameEventTaskExecutor(GameManagerFactory gameManagerFactory, ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = checkNotNull(scheduledExecutorService);
        this.sessionProcessor = gameManagerFactory;
    }

    public void schedule(GameEventTask eventTask) {
        // Step 1. Canceling existing task
        cancel(eventTask);
        // Step 2. Calculating start delay for the next execution
        long startDelay = Math.max(0, eventTask.nextExecutionTime(null).getTime() - System.currentTimeMillis());
        // Step 3. Saving mapping for reprocessing
        ScheduledFuture<?> nextExecution = scheduledExecutorService.schedule(new GameEventTaskWrapper(eventTask), startDelay, TimeUnit.MILLISECONDS);
        concurrentHashMap.put(eventTask, nextExecution);
    }

    public void reschedule(GameEventTask eventTask) {
        schedule(eventTask);
    }

    public void cancel(GameEventTask eventTask) {
        // Step 1. Canceling existing task
        ScheduledFuture<?> existingTask = concurrentHashMap.remove(eventTask);
        if (existingTask != null)
            existingTask.cancel(false);
    }

    public class GameEventTaskWrapper implements Runnable {
        final private GameEventTask eventTask;

        public GameEventTaskWrapper(GameEventTask eventTask) {
            this.eventTask = checkNotNull(eventTask);
        }

        @Override
        public void run() {
            Collection<Event> events = eventTask.execute();
            GameManager<?> manager = GameEventTaskExecutor.this.sessionProcessor.get(eventTask.getSessionKey());
            for (Event event : events) {
                manager.process(event);
            }
            reschedule(eventTask);
        }
    }

}
