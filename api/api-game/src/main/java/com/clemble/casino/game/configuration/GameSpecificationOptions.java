package com.clemble.casino.game.configuration;

import com.clemble.casino.game.GameAware;
import com.clemble.casino.game.specification.GameSpecification;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface GameSpecificationOptions extends GameAware {

    public boolean valid(GameSpecification specification);

}