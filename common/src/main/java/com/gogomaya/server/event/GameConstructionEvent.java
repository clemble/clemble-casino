package com.gogomaya.server.event;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.game.ConstructionAware;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface GameConstructionEvent extends Event, ConstructionAware {

}
