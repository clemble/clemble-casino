package com.gogomaya.server.game.rule.bet;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.rule.bet.BetRuleFormat.CustomBetRuleDeserializer;
import com.gogomaya.server.game.rule.bet.BetRuleFormat.CustomBetRuleSerializer;
import com.gogomaya.server.player.wallet.CashType;

@JsonSerialize(using = CustomBetRuleSerializer.class)
@JsonDeserialize(using = CustomBetRuleDeserializer.class)
final public class LimitedBetRule extends BetRule {

    final private long minBet;

    final private long maxBet;

    private LimitedBetRule(final CashType cashType, final long minBet, final long maxBet) {
        super(BetType.Limited, cashType);
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

    public static LimitedBetRule create(CashType cashType, long minBet, long maxBet) {
        return new LimitedBetRule(cashType, minBet, maxBet);
    }

}