package com.clemble.casino.server.player.notification;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface PlayerNotificationRegistry extends Serializable {

    public String findNotificationServer(String player);

}
