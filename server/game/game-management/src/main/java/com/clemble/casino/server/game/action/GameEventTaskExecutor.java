package com.clemble.casino.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.clemble.casino.game.event.client.GameAction;

public class GameEventTaskExecutor implements BeanPostProcessor {

    private GameSessionProcessor<?> sessionProcessor;
    final private ConcurrentHashMap<GameEventTask, ScheduledFuture<?>> concurrentHashMap = new ConcurrentHashMap<>();

    final private ScheduledExecutorService scheduledExecutorService;

    public GameEventTaskExecutor(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = checkNotNull(scheduledExecutorService);
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
            Collection<GameAction> events = eventTask.execute();
            for (GameAction event : events) {
                GameEventTaskExecutor.this.sessionProcessor.process(eventTask.getSession(), event);
            }
            reschedule(eventTask);
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof GameSessionProcessor) {
            sessionProcessor = (GameSessionProcessor<?>) bean;
        }
        return bean;
    }

}
