package com.gogomaya.server.game.rule.giveup;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat.CustomGiveUpRuleDeserializer;
import com.gogomaya.server.game.rule.giveup.GiveUpRuleFormat.CustomGiveUpRuleSerializer;

@JsonSerialize(using = CustomGiveUpRuleSerializer.class)
@JsonDeserialize(using = CustomGiveUpRuleDeserializer.class)
final public class LooseLostGiveUpRule extends GiveUpRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = -3416635969563391335L;

    private LooseLostGiveUpRule() {
        super(LoosingType.Lost);
    }

    public static LooseLostGiveUpRule INSTANCE = new LooseLostGiveUpRule();

}