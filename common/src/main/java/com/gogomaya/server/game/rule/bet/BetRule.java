package com.gogomaya.server.game.rule.bet;

import static com.google.common.base.Preconditions.checkNotNull;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.rule.GameRule;
import com.gogomaya.server.game.rule.bet.BetRuleFormat.CustomBetRuleDeserializer;
import com.gogomaya.server.game.rule.bet.BetRuleFormat.CustomBetRuleSerializer;

@JsonSerialize(using = CustomBetRuleSerializer.class)
@JsonDeserialize(using = CustomBetRuleDeserializer.class)
abstract public class BetRule implements GameRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = -1458557269866528512L;

    final private BetType ruleType;

    protected BetRule(final BetType betType) {
        this.ruleType = checkNotNull(betType);
    }

    public BetType getRuleType() {
        return ruleType;
    }

}
