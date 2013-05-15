package com.gogomaya.server.game.rule.bet;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.gogomaya.server.game.rule.GameRule;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "betType")
@JsonSubTypes({
    @Type(name = "fixed", value = FixedBetRule.class),
    @Type(name = "limited", value = LimitedBetRule.class),
    @Type(name = "unlimited", value = UnlimitedBetRule.class) })
public interface BetRule extends GameRule {

}
