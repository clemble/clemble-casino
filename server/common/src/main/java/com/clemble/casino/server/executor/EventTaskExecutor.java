package com.clemble.casino.server.executor;

import com.clemble.casino.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

public class EventTaskExecutor {

    final private Logger LOG = LoggerFactory.getLogger(EventTaskExecutor.class);

    final private EventTaskAdapter taskAdapter;
    final private ConcurrentHashMap<EventTask, ScheduledFuture<?>> concurrentHashMap = new ConcurrentHashMap<>();

    final private ScheduledExecutorService scheduledExecutorService;

    public EventTaskExecutor(EventTaskAdapter taskAdapter, ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = checkNotNull(scheduledExecutorService);
        this.taskAdapter = taskAdapter;
    }

    public void schedule(EventTask eventTask) {
        LOG.debug("{} scheduling", eventTask);
        // Step 1. Canceling existing task
        cancel(eventTask);
        // Step 2. Calculating start delay for the next execution
        long startDelay = Math.max(0, eventTask.nextExecutionTime(null).getTime() - System.currentTimeMillis());
        LOG.debug("{} startDelay {}", eventTask, startDelay);
        // Step 3. Saving mapping for reprocessing
        ScheduledFuture<?> nextExecution = scheduledExecutorService.schedule(new EventTaskWrapper(eventTask), startDelay, TimeUnit.MILLISECONDS);
        LOG.debug("{} created nextExecution {}", eventTask, nextExecution);
        concurrentHashMap.put(eventTask, nextExecution);
    }

    public void reschedule(EventTask eventTask) {
        LOG.debug("{} reScheduling", eventTask);
        schedule(eventTask);
    }

    public void cancel(EventTask eventTask) {
        // Step 1. Canceling existing task
        ScheduledFuture<?> existingTask = concurrentHashMap.remove(eventTask);
        if (existingTask != null) {
            LOG.debug("{} canceling ", eventTask);
            existingTask.cancel(false);
        } else {
            LOG.warn("{} nothing to cancel", eventTask);
        }
    }

    public class EventTaskWrapper implements Runnable {
        final private EventTask eventTask;

        public EventTaskWrapper(EventTask eventTask) {
            this.eventTask = checkNotNull(eventTask);
        }

        @Override
        public void run() {
            // Step 1. Processing through event task adapter
            LOG.warn("{} running ", eventTask);
            taskAdapter.process(eventTask);
            // !!! NO RESCHEDULING AFTER INITIAL EXECUTION
        }

    }

}
