package com.gogomaya.server.game.rule.bet;

import static com.google.common.base.Preconditions.checkNotNull;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.GameRule;
import com.gogomaya.server.game.rule.bet.BetRuleFormat.CustomBetRuleDeserializer;
import com.gogomaya.server.game.rule.bet.BetRuleFormat.CustomBetRuleSerializer;
import com.gogomaya.server.player.wallet.CashType;

@JsonSerialize(using = CustomBetRuleSerializer.class)
@JsonDeserialize(using = CustomBetRuleDeserializer.class)
abstract public class BetRule implements GameRule {

    final private CashType cashType;

    final private BetType ruleType;

    protected BetRule(final BetType betType, final CashType cashType) {
        this.cashType = checkNotNull(cashType);
        this.ruleType = checkNotNull(betType);
    }

    public CashType getCashType() {
        return cashType;
    }

    public BetType getRuleType() {
        return ruleType;
    }

}
