package com.gogomaya.server.event;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.player.PlayerAware;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface ClientEvent extends PlayerAware, Event {

}
