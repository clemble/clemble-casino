package com.gogomaya.server.event;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.game.event.server.GameEndedEvent;
import com.gogomaya.server.game.event.server.GameStartedEvent;
import com.gogomaya.server.game.event.server.PlayerAddedEvent;
import com.gogomaya.server.game.event.server.PlayerLostEvent;
import com.gogomaya.server.game.event.server.PlayerMovedEvent;
import com.gogomaya.server.game.match.event.InvitationAcceptedEvent;
import com.gogomaya.server.game.match.event.InvitationDeclinedEvent;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "event")
@JsonSubTypes({ @Type(value = GameEndedEvent.class, name = "ended"),
    @Type(value = GameStartedEvent.class, name = "started"),
    @Type(value = PlayerLostEvent.class, name = "playerGaveUp"),
    @Type(value = PlayerMovedEvent.class, name = "playerMoved"),
    @Type(value = PlayerAddedEvent.class, name = "playerAdded"),
    @Type(value = InvitationAcceptedEvent.class, name = "invitationAccepted"),
    @Type(value = InvitationDeclinedEvent.class, name = "invitationDeclined")
})
public interface ServerEvent extends Serializable {

    public Date getPublishDate();

}
