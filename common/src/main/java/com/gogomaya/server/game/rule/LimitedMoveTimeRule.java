package com.gogomaya.server.game.rule;


public class LimitedMoveTimeRule extends TimeRule {
    final private int moveTimeLimit;

    public LimitedMoveTimeRule(final int timeLimit) {
        super(TimeRuleBreachBehavior.Fail);
        this.moveTimeLimit = timeLimit;
    }

    public int getMoveTimeLimit() {
        return moveTimeLimit;
    }
}