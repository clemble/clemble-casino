package com.gogomaya.server.game;

import java.io.Serializable;

public interface GameSpecificationAware extends Serializable {

    GameSpecification getSpecification();

}
