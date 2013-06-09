package com.gogomaya.server.game.rule.time.action;

import java.util.concurrent.PriorityBlockingQueue;

public class TimeRuleScheduler {

    final private PriorityBlockingQueue<TimeTask> timeQueue = new PriorityBlockingQueue<TimeTask>();

    public void schedule(long session, long nextCheckMillis) {
        TimeTask newTimeTask = new TimeTask(session, System.currentTimeMillis() + nextCheckMillis);
        timeQueue.remove(newTimeTask);
        timeQueue.add(newTimeTask);
    }

    public TimeTask poll() {
        if (timeQueue.peek().getCheckTime() < System.currentTimeMillis()) {
            return timeQueue.poll();
        }
        return null;
    }
}
