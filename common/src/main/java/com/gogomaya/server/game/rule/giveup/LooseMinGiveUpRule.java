package com.gogomaya.server.game.rule.giveup;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat.CustomGiveUpRuleDeserializer;
import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat.CustomGiveUpRuleSerializer;

@JsonSerialize(using = CustomGiveUpRuleSerializer.class)
@JsonDeserialize(using = CustomGiveUpRuleDeserializer.class)
final public class LooseMinGiveUpRule extends GiveUpRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = 400326180244339700L;

    final private int minPart;

    public LooseMinGiveUpRule(final int minPart) {
        super(LoosingType.MinPart);
        if (minPart > 100)
            throw new IllegalArgumentException("Min part can't exceed 100");
        if (minPart < 0)
            throw new IllegalArgumentException("Min part can't be less than 0");
        this.minPart = minPart;
    }

    public int getMinPart() {
        return minPart;
    }

    public static LooseMinGiveUpRule create(int minPart) {
        return new LooseMinGiveUpRule(minPart);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + minPart;
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
        LooseMinGiveUpRule other = (LooseMinGiveUpRule) obj;
        if (minPart != other.minPart)
            return false;
        return true;
    }

}