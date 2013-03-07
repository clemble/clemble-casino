package com.gogomaya.server.game.rule.giveup;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat.CustomGiveUpRuleDeserializer;
import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat.CustomGiveUpRuleSerializer;

@JsonSerialize(using = CustomGiveUpRuleSerializer.class)
@JsonDeserialize(using = CustomGiveUpRuleDeserializer.class)
final public class LooseMinGiveUpRule extends GiveUpRule {

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

}