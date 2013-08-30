package com.gogomaya.game.configuration;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.game.GameAware;
import com.gogomaya.game.specification.GameSpecification;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface GameSpecificationOptions extends GameAware {

    public boolean valid(GameSpecification specification);

}