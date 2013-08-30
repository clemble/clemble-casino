package com.gogomaya.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.Game;

public class GameEventTaskExecutor implements BeanPostProcessor {

    final private Map<Game, GameSessionProcessor<?>> gameToProcessor = new HashMap<>();
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
            Collection<ClientEvent> events = eventTask.execute();
            for (ClientEvent event : events) {
                GameEventTaskExecutor.this.gameToProcessor.get(eventTask.getGame()).process(eventTask.getSession(), event);
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
            GameSessionProcessor<?> processor = (GameSessionProcessor<?>) bean;
            if (gameToProcessor.containsKey(processor.getGame()))
                throw new IllegalArgumentException("Can't be 2 processors for the same game " + processor.getGame() + " / " + processor);
            gameToProcessor.put(processor.getGame(), processor);
        }
        return bean;
    }

}
