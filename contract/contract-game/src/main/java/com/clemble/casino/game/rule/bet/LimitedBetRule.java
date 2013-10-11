package com.clemble.casino.game.rule.bet;

import com.clemble.casino.game.event.client.BetEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("limited")
public class LimitedBetRule implements BetRule {

    /**
     * Generated 09/04/13
     */
    final static private long serialVersionUID = -5560244451652751412L;

    final private int minBet;

    final private int maxBet;

    @JsonIgnore
    private LimitedBetRule(final int minBet, final int maxBet) {
        if (minBet > maxBet)
            throw new IllegalArgumentException("MIN bet can't be lesser, than MAX bet");
        if (minBet < 0)
            throw new IllegalArgumentException("MIN bet can't be lesser, than 0");
        this.minBet = minBet;
        this.maxBet = maxBet;
    }

    public int getMinBet() {
        return minBet;
    }

    public int getMaxBet() {
        return maxBet;
    }

    @JsonCreator
    public static LimitedBetRule create(@JsonProperty("min") int minBet, @JsonProperty("max") int maxBet) {
        return new LimitedBetRule(minBet, maxBet);
    }

    @Override
    public boolean isValid(BetEvent betEvent) {
        return minBet <= betEvent.getBet() && betEvent.getBet() <= maxBet;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + maxBet;
        result = prime * result + minBet;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LimitedBetRule other = (LimitedBetRule) obj;
        if (maxBet != other.maxBet)
            return false;
        if (minBet != other.minBet)
            return false;
        return true;
    }

}