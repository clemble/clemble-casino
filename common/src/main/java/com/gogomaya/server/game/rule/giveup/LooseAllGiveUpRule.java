package com.gogomaya.server.game.rule.giveup;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat.CustomGiveUpRuleDeserializer;
import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat.CustomGiveUpRuleSerializer;

@JsonSerialize(using = CustomGiveUpRuleSerializer.class)
@JsonDeserialize(using = CustomGiveUpRuleDeserializer.class)
final public class LooseAllGiveUpRule extends GiveUpRule {

    protected LooseAllGiveUpRule() {
        super(LoosingType.All);
    }

    public static LooseAllGiveUpRule INSTANCE = new LooseAllGiveUpRule();

}