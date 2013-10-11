package com.clemble.casino.game.rule.bet;

import com.clemble.casino.game.event.client.BetEvent;
import com.clemble.casino.game.rule.GameRule;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("bet")
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "betType")
public interface BetRule extends GameRule {

    public boolean isValid(BetEvent betEvent);

}
