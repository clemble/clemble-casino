package com.gogomaya.server.game.rule.bet;

import java.util.concurrent.ExecutionException;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

final public class FixedBetRule extends BetRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = 6656576325438885626L;

    final private static LoadingCache<Integer, FixedBetRule> INSTANCE_CACHE = CacheBuilder.newBuilder().build(new CacheLoader<Integer, FixedBetRule>() {

        @Override
        public FixedBetRule load(Integer priceToUse) throws Exception {
            return new FixedBetRule(priceToUse);
        }

    });

    final private int price;

    private FixedBetRule(final int priceToUse) {
        if (priceToUse < 0)
            throw GogomayaException.create(GogomayaError.ClientJsonFormatError);
        this.price = priceToUse;
    }

    public int getPrice() {
        return price;
    }

    @JsonCreator
    public static FixedBetRule create(@JsonProperty("price") int price) {
        try {
            return INSTANCE_CACHE.get(price);
        } catch (ExecutionException e) {
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }
    }

}