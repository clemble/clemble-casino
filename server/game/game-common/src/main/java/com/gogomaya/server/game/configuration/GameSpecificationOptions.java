package com.gogomaya.server.game.configuration;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.game.GameAware;
import com.gogomaya.server.game.specification.GameSpecification;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface GameSpecificationOptions extends GameAware {

    public boolean valid(GameSpecification specification);

}