package com.gogomaya.server.game.rule.giveup;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import com.gogomaya.server.game.rule.GameRule;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "looseType")
@JsonSubTypes({
    @Type(name = "all", value = GiveUpAllRule.class),
    @Type(name = "lost", value = GiveUpLostRule.class),
    @Type(name = "min", value = GiveUpLeastRule.class) })
abstract public class GiveUpRule implements GameRule {

    /**
     * Generated 09/04/13
     */
    private static final long serialVersionUID = -7106595644249003313L;

    protected GiveUpRule() {
    }

}
