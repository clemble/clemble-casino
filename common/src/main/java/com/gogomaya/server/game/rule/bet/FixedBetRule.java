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
final public class FixedBetRule extends BetRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = 6656576325438885626L;

    final private static LoadingCache<Long, FixedBetRule> INSTANCE_CACHE = CacheBuilder.newBuilder().build(new CacheLoader<Long, FixedBetRule>() {

        @Override
        public FixedBetRule load(Long priceToUse) throws Exception {
            return new FixedBetRule(priceToUse);
        }

    });

    final private long price;

    private FixedBetRule(final long priceToUse) {
        super(BetType.Fixed);
        if (priceToUse < 0)
            throw GogomayaException.create(GogomayaError.ClientJsonFormatError);
        this.price = priceToUse;
    }

    public long getPrice() {
        return price;
    }

    public static FixedBetRule create(long price) {
        try {
            return INSTANCE_CACHE.get(price);
        } catch (ExecutionException e) {
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }
    }

    @Override
    public BetType getRuleType() {
        return BetType.Fixed;
    }

}