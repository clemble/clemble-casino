package com.gogomaya.server.game;

import java.util.List;

public interface SelectSpecificationOptions extends GameSpecificationOptions {

    public List<? extends GameSpecification> getSpecifications();

}
