package com.gogomaya.server.game.bet.rule;

import java.util.concurrent.ExecutionException;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

final public class BetLimitedRule extends BetRule {

    /**
     * Generated 09/04/13
     */
    final static private long serialVersionUID = -5560244451652751412L;

    final private static LoadingCache<Long, BetLimitedRule> INSTANCE_CACHE = CacheBuilder.newBuilder().build(new CacheLoader<Long, BetLimitedRule>() {

        @Override
        public BetLimitedRule load(Long entry) throws Exception {
            return new BetLimitedRule((int) (entry >> 32), (int) (entry & 0x00000000FFFFFFFFL));
        }

    });

    final private int minBet;

    final private int maxBet;

    @JsonIgnore
    private BetLimitedRule(final int minBet, final int maxBet) {
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
    public static BetLimitedRule create(@JsonProperty("min") int minBet, @JsonProperty("max") int maxBet) {
        try {
            return INSTANCE_CACHE.get(((long) minBet << 32) | maxBet);
        } catch (ExecutionException e) {
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }
    }

}