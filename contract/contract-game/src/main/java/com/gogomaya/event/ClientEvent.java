package com.gogomaya.event;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.event.Event;
import com.gogomaya.player.PlayerAware;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface ClientEvent extends PlayerAware, Event {

}
