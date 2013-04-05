package com.gogomaya.server.game.rule.giveup;

import static com.google.common.base.Preconditions.checkNotNull;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.rule.GameRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat.CustomGiveUpRuleDeserializer;
import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat.CustomGiveUpRuleSerializer;

@JsonSerialize(using = CustomGiveUpRuleSerializer.class)
@JsonDeserialize(using = CustomGiveUpRuleDeserializer.class)
abstract public class GiveUpRule implements GameRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = -7106595644249003313L;

    final private LoosingType loosingType;

    protected GiveUpRule(LoosingType loosingType) {
        this.loosingType = checkNotNull(loosingType);
    }

    public LoosingType getLoosingType() {
        return loosingType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((loosingType == null) ? 0 : loosingType.hashCode());
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
        GiveUpRule other = (GiveUpRule) obj;
        if (loosingType != other.loosingType)
            return false;
        return true;
    }

}
