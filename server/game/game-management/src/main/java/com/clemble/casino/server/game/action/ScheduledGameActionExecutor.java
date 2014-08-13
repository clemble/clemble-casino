package com.clemble.casino.server.game.action;

import java.util.concurrent.PriorityBlockingQueue;

public class ScheduledGameActionExecutor implements Runnable {

    final private PriorityBlockingQueue<ScheduledGameAction> scheduledActions = new PriorityBlockingQueue<>();
    final private GameManagerFactory sessionProcessor;

    public ScheduledGameActionExecutor(GameManagerFactory gameManagerFactory) {
        this.sessionProcessor = gameManagerFactory;
    }

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
            sessionProcessor.get(action.getSessionKey()).process(action.getAction());
        }
    }

}
