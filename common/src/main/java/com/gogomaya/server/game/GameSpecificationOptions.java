package com.gogomaya.server.game;

import java.io.Serializable;

public interface GameSpecificationOptions extends Serializable {

    public boolean valid(GameSpecification specification);

}