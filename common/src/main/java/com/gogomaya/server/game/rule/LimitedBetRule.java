package com.gogomaya.server.game.rule;


public class LimitedBetRule extends BetRule {

    private long minBet;

    private long maxBet;

    public long getMinBet() {
        return minBet;
    }

    public void setMinBet(long minBet) {
        this.minBet = minBet;
    }

    public long getMaxBet() {
        return maxBet;
    }

    public void setMaxBet(long maxBet) {
        this.maxBet = maxBet;
    }
}
