package com.gogomaya.server.game.rule.bet;

import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.rule.bet.BetRuleFormat.CustomBetRuleDeserializer;
import com.gogomaya.server.game.rule.bet.BetRuleFormat.CustomBetRuleSerializer;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@JsonSerialize(using = CustomBetRuleSerializer.class)
@JsonDeserialize(using = CustomBetRuleDeserializer.class)
final public class LimitedBetRule extends BetRule {

    /**
     * Generated 09/04/13
     */
    final static private long serialVersionUID = -5560244451652751412L;

    final private static LoadingCache<Entry<Long, Long>, LimitedBetRule> INSTANCE_CACHE = CacheBuilder.newBuilder().build(
            new CacheLoader<Entry<Long, Long>, LimitedBetRule>() {

                @Override
                public LimitedBetRule load(Entry<Long, Long> entry) throws Exception {
                    return new LimitedBetRule(entry.getKey(), entry.getValue());
                }

            });

    final private long minBet;

    final private long maxBet;

    private LimitedBetRule(final long minBet, final long maxBet) {
        super(BetType.Limited);
        if (minBet > maxBet)
            throw new IllegalArgumentException("MIN bet can't be lesser, than MAX bet");
        if (minBet < 0)
            throw new IllegalArgumentException("MIN bet can't be lesser, than 0");
        this.minBet = minBet;
        this.maxBet = maxBet;
    }

    public long getMinBet() {
        return minBet;
    }

    public long getMaxBet() {
        return maxBet;
    }

    public static LimitedBetRule create(long minBet, long maxBet) {
        try {
            return INSTANCE_CACHE.get(new ImmutablePair<Long, Long>(minBet, maxBet));
        } catch (ExecutionException e) {
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }
    }

}