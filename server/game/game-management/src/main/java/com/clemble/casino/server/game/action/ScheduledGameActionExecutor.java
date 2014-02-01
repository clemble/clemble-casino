package com.clemble.casino.server.game.action;

import java.util.concurrent.PriorityBlockingQueue;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class ScheduledGameActionExecutor implements Runnable, BeanPostProcessor {

    final private PriorityBlockingQueue<ScheduledGameAction> scheduledActions = new PriorityBlockingQueue<>();

    private MatchGameManager<?> sessionProcessor;

    public void schedule(ScheduledGameAction action) {
        this.scheduledActions.add(action);
    }

    public void cancel(ScheduledGameAction action) {
        this.scheduledActions.remove(action);
    }

    @Override
    public void run() {
        while (true) {
            ScheduledGameAction action = scheduledActions.peek();
            if (action == null || action.getScheduled().getTime() > System.currentTimeMillis())
                return;
            action = scheduledActions.poll();
            if (action == null || action.getScheduled().getTime() > System.currentTimeMillis()) {
                scheduledActions.add(action);
                return;
            }
            sessionProcessor.process(action.getSession(), action.getAction());
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MatchGameManager)
            sessionProcessor = (MatchGameManager<?>) bean;
        return bean;
    }

}
