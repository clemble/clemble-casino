package com.gogomaya.server.game.rule;


public class LimitedGameTimeRule extends TimeRule {

    final private int gameTimeLimit;

    public LimitedGameTimeRule(final int timeLimit) {
        super(TimeRuleBreachBehavior.Fail);
        this.gameTimeLimit = timeLimit;
    }

    public int getGameTimeLimit() {
        return gameTimeLimit;
    }
}