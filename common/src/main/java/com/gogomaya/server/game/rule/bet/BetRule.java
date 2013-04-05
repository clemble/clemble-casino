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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ruleType == null) ? 0 : ruleType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BetRule other = (BetRule) obj;
        if (ruleType != other.ruleType)
            return false;
        return true;
    }

}
