package com.gogomaya.server.game.active.time;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.rule.time.MoveTimeRule;

public class MoveTimeTracker extends TimeTracker {

    final private MoveTimeRule moveTimeRule;

    public MoveTimeTracker(long session, MoveTimeRule moveTimeRule) {
        super(session);
        this.moveTimeRule = checkNotNull(moveTimeRule);
    }

    @Override
    public void startTracking(long player) {
        add(new GameTimeBreach(player, System.currentTimeMillis() + moveTimeRule.getLimit(), moveTimeRule));
    }

    @Override
    public void stopTracking(long player, long moveTime) {
    }

}
