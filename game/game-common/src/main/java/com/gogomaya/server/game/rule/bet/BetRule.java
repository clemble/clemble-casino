package com.gogomaya.server.game.rule.bet;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.game.rule.GameRule;

@JsonTypeName("bet")
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "betType")
public interface BetRule extends GameRule {

}
