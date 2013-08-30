package com.gogomaya.event;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.game.SessionAware;
import com.gogomaya.server.event.Event;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface GameConstructionEvent extends Event, SessionAware {

}
