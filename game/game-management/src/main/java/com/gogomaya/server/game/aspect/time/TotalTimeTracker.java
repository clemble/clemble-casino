package com.gogomaya.server.game.aspect.time;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;

import com.gogomaya.server.game.rule.time.TotalTimeRule;

public class TotalTimeTracker extends TimeTracker {

    /**
     * Generated 20/07/13
     */
    private static final long serialVersionUID = -5174948343979628680L;

    final private TotalTimeRule totalTimeRule;
    final private HashMap<Long, Long> totalTime = new HashMap<Long, Long>();

    public TotalTimeTracker(long session, TotalTimeRule totalTimeRule) {
        super(session);
        this.totalTimeRule = checkNotNull(totalTimeRule);
    }

    @Override
    public void startTracking(long player) {
        add(new GameTimeBreach(player, System.currentTimeMillis() + (totalTimeRule.getLimit() - getTotalTime(player)), totalTimeRule));
    }

    @Override
    public void stopTracking(long player, long moveTime) {
        totalTime.put(player, getTotalTime(player) + moveTime);
    }

    private long getTotalTime(long player) {
        Long totalTimeValue = totalTime.get(player);
        return (totalTimeValue == null ? 0 : totalTimeValue) + getMoveTime(player);
    }

}
