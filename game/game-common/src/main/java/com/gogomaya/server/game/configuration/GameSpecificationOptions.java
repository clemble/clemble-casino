package com.gogomaya.server.game.configuration;

import java.io.Serializable;

import com.gogomaya.server.game.specification.GameSpecification;

public interface GameSpecificationOptions extends Serializable {

    public boolean valid(GameSpecification specification);

}