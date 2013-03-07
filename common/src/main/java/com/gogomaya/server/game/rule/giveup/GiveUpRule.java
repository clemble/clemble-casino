package com.gogomaya.server.game.rule.giveup;

import static com.google.common.base.Preconditions.checkNotNull;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.GameRule;
import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat.CustomGiveUpRuleDeserializer;
import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat.CustomGiveUpRuleSerializer;

@JsonSerialize(using = CustomGiveUpRuleSerializer.class)
@JsonDeserialize(using = CustomGiveUpRuleDeserializer.class)
abstract public class GiveUpRule implements GameRule {

    final private LoosingType loosingType;

    protected GiveUpRule(LoosingType loosingType) {
        this.loosingType = checkNotNull(loosingType);
    }

    public LoosingType getLoosingType() {
        return loosingType;
    }

}
