package com.gogomaya.server.game.rule.time.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTimeState;
import com.gogomaya.server.game.GameTimeState.TimeBreach;
import com.gogomaya.server.game.GameTimeState.TotalTimeBreach;
import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.game.event.client.MoveTimeBreachedEvent;
import com.gogomaya.server.game.event.client.TotalTimeBreachedEvent;

public class GameTimeRuleJudge<State extends GameState> implements Serializable, Runnable {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = 5594629008080772700L;

    final private ConcurrentSkipListMap<Long, GameTimeState> timeQueue = new ConcurrentSkipListMap<Long, GameTimeState>();

    final private GameSessionProcessor<State> sessionProcessor;
    final private TimeTaskFactory<State> taskFactory;

    public GameTimeRuleJudge(final TimeTaskFactory<State> taskFactory, final GameSessionProcessor<State> sessionProcessor) {
        this.taskFactory = checkNotNull(taskFactory);
        this.sessionProcessor = checkNotNull(sessionProcessor);
    }

    public GameTimeState fetch(final GameSession<State> session) {
        GameTimeState task = timeQueue.remove(session);
        if (task == null) {
            task = taskFactory.construct(session);
        }
        return task;
    }

    public void put(final long session, final GameTimeState newTimeTask) {
        timeQueue.put(session, newTimeTask);
    }

    public GameTimeState poll() {
        // Step 1. Polling first entry
        Entry<Long, GameTimeState> firstEntry = timeQueue.pollFirstEntry();
        if (firstEntry != null) {
            // Step 2. Checking when this entry supposed to be called
            GameTimeState timeTask = firstEntry.getValue();
            if (System.currentTimeMillis() >= firstEntry.getValue().getCheckTime()) {
                return firstEntry.getValue();
            }
            // Step 3. Return entry to the Queue
            this.timeQueue.putIfAbsent(timeTask.getSession(), timeTask);
        }
        return null;
    }

    @Override
    public void run() {
        GameTimeState nextTask = poll();
        while (nextTask != null) {
            TimeBreach timeBreach = nextTask.poll();
            if (timeBreach.getBreachTime() > System.currentTimeMillis()) {
                throw new IllegalArgumentException();
            } else {
                if (timeBreach instanceof TotalTimeBreach) {
                    sessionProcessor.process(nextTask.getSession(), new TotalTimeBreachedEvent(timeBreach.getPlayerId(), nextTask));
                } else {
                    sessionProcessor.process(nextTask.getSession(), new MoveTimeBreachedEvent(timeBreach.getPlayerId(), nextTask));
                }
            }
            nextTask = poll();
        }
    }
}
