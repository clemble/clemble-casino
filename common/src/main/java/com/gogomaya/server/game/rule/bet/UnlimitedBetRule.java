package com.gogomaya.server.game.rule.bet;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.rule.bet.BetRuleFormat.CustomBetRuleDeserializer;
import com.gogomaya.server.game.rule.bet.BetRuleFormat.CustomBetRuleSerializer;
import com.gogomaya.server.player.wallet.CashType;

@JsonSerialize(using = CustomBetRuleSerializer.class)
@JsonDeserialize(using = CustomBetRuleDeserializer.class)
final public class UnlimitedBetRule extends BetRule {

    private UnlimitedBetRule(CashType cashType) {
        super(BetType.Unlimited, cashType);
    }

    public static UnlimitedBetRule create(CashType cashType) {
        return new UnlimitedBetRule(cashType);
    }

}