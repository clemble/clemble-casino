package com.gogomaya.server.event;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.game.event.GameEndedEvent;
import com.gogomaya.server.game.event.GameStartedEvent;
import com.gogomaya.server.game.event.PlayerAddedEvent;
import com.gogomaya.server.game.event.PlayerGaveUpEvent;
import com.gogomaya.server.game.event.PlayerMovedEvent;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "event")
@JsonSubTypes({ @Type(value = GameEndedEvent.class, name = "ended"),
    @Type(value = GameStartedEvent.class, name = "started"),
    @Type(value = PlayerGaveUpEvent.class, name = "playerGaveUp"),
    @Type(value = PlayerAddedEvent.class, name = "playerAdded"),
    @Type(value = PlayerMovedEvent.class, name = "playerMoved")})
public interface GogomayaEvent extends Serializable {

    public Date getPublishDate();

}
