package com.clemble.casino.event;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.SessionAware;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface ConstructionEvent extends Event, SessionAware {

}
