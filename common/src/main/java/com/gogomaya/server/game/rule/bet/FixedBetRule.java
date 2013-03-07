package com.gogomaya.server.game.rule.bet;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.rule.bet.BetRuleFormat.CustomBetRuleDeserializer;
import com.gogomaya.server.game.rule.bet.BetRuleFormat.CustomBetRuleSerializer;
import com.gogomaya.server.player.wallet.CashType;

@JsonSerialize(using = CustomBetRuleSerializer.class)
@JsonDeserialize(using = CustomBetRuleDeserializer.class)
final public class FixedBetRule extends BetRule {

    final private long price;

    private FixedBetRule(final CashType cashType, final long priceToUse) {
        super(BetType.Fixed, cashType);
        if (priceToUse < 0)
            throw GogomayaException.create(GogomayaError.ClientJsonFormatError);
        this.price = priceToUse;
    }

    public long getPrice() {
        return price;
    }

    public static FixedBetRule create(CashType cashType, long price) {
        return new FixedBetRule(cashType, price);
    }

}