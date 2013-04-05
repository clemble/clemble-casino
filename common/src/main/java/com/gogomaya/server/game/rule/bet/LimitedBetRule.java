package com.gogomaya.server.game.rule.bet;

import java.util.concurrent.ExecutionException;

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

    final private static LoadingCache<Long, LimitedBetRule> INSTANCE_CACHE = CacheBuilder.newBuilder().build(new CacheLoader<Long, LimitedBetRule>() {

        @Override
        public LimitedBetRule load(Long entry) throws Exception {
            return new LimitedBetRule((int) (entry >> 32), (int) (entry & 0x00000000FFFFFFFFL));
        }

    });

    final private int minBet;

    final private int maxBet;

    private LimitedBetRule(final int minBet, final int maxBet) {
        super(BetType.Limited);
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

    public static LimitedBetRule create(int minBet, int maxBet) {
        try {
            return INSTANCE_CACHE.get(((long) minBet << 32) | maxBet);
        } catch (ExecutionException e) {
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + maxBet;
        result = prime * result + minBet;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
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