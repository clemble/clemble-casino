package com.gogomaya.server.game.rule.time.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameState;

public class TimeRuleScheduler<State extends GameState> {

    final private ConcurrentSkipListMap<Long, TimeTask> timeQueue = new ConcurrentSkipListMap<Long, TimeTask>();

    final private TimeTaskFactory<State> taskFactory;

    public TimeRuleScheduler(TimeTaskFactory<State> taskFactory) {
        this.taskFactory = checkNotNull(taskFactory);
    }

    public TimeTask fetch(final GameSession<State> session) {
        TimeTask task = timeQueue.remove(session);
        if (task == null) {
            task = taskFactory.construct(session);
        }
        return task;
    }

    public void put(final long session, final TimeTask newTimeTask) {
        timeQueue.put(session, newTimeTask);
    }

    public TimeTask poll() {
        // Step 1. Polling first entry
        Entry<Long, TimeTask> firstEntry = timeQueue.pollFirstEntry();
        if (firstEntry != null) {
            // Step 2. Checking when this entry supposed to be called
            TimeTask timeTask = firstEntry.getValue();
            if (System.currentTimeMillis() >= firstEntry.getValue().getCheckTime()) {
                return firstEntry.getValue();
            }
            // Step 3. Return entry to the Queue
            this.timeQueue.putIfAbsent(timeTask.getSession(), timeTask);
        }
        return null;
    }
}
