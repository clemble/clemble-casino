package com.clemble.casino.server.executor;

import com.clemble.casino.event.Event;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

public class EventTaskExecutor {

    final private EventTaskAdapter taskAdapter;
    final private ConcurrentHashMap<EventTask, ScheduledFuture<?>> concurrentHashMap = new ConcurrentHashMap<>();

    final private ScheduledExecutorService scheduledExecutorService;

    public EventTaskExecutor(EventTaskAdapter taskAdapter, ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = checkNotNull(scheduledExecutorService);
        this.taskAdapter = taskAdapter;
    }

    public void schedule(EventTask eventTask) {
        // Step 1. Canceling existing task
        cancel(eventTask);
        // Step 2. Calculating start delay for the next execution
        long startDelay = Math.max(0, eventTask.nextExecutionTime(null).getTime() - System.currentTimeMillis());
        // Step 3. Saving mapping for reprocessing
        ScheduledFuture<?> nextExecution = scheduledExecutorService.schedule(new EventTaskWrapper(eventTask), startDelay, TimeUnit.MILLISECONDS);
        concurrentHashMap.put(eventTask, nextExecution);
    }

    public void reschedule(EventTask eventTask) {
        schedule(eventTask);
    }

    public void cancel(EventTask eventTask) {
        // Step 1. Canceling existing task
        ScheduledFuture<?> existingTask = concurrentHashMap.remove(eventTask);
        if (existingTask != null)
            existingTask.cancel(false);
    }

    public class EventTaskWrapper implements Runnable {
        final private EventTask eventTask;

        public EventTaskWrapper(EventTask eventTask) {
            this.eventTask = checkNotNull(eventTask);
        }

        @Override
        public void run() {
            // Step 1. Processing through event task adapter
            taskAdapter.process(eventTask);
            // Step 2. Rescheduling for future events
            reschedule(eventTask);
        }

    }

}
