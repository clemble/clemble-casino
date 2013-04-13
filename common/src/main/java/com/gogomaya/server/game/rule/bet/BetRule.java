package com.gogomaya.server.game.rule.bet;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import com.gogomaya.server.game.rule.GameRule;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "betType")
@JsonSubTypes({
    @Type(name = "fixed", value = FixedBetRule.class),
    @Type(name = "limited", value = LimitedBetRule.class),
    @Type(name = "unlimited", value = UnlimitedBetRule.class) })
public interface BetRule extends GameRule {

}
