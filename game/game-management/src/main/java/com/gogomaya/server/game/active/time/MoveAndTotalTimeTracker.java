package com.gogomaya.server.game.active.time;

import java.util.HashMap;

import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TotalTimeRule;

public class MoveAndTotalTimeTracker extends TimeTracker {

    /**
     * Generated 20/07/13
     */
    private static final long serialVersionUID = -7681063590268707575L;

    final private TotalTimeRule totalTimeRule;
    final private MoveTimeRule moveTimeRule;

    final private HashMap<Long, Long> totalTime = new HashMap<Long, Long>();

    public MoveAndTotalTimeTracker(long session, TotalTimeRule totalTimeRule, MoveTimeRule moveTimeRule) {
        super(session);
        this.totalTimeRule = totalTimeRule;
        this.moveTimeRule = moveTimeRule;
    }

    @Override
    public void startTracking(long player) {
        add(new GameTimeBreach(player, System.currentTimeMillis() + moveTimeRule.getLimit(), moveTimeRule));
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
