package com.clemble.casino.server.event;

import com.clemble.casino.event.Event;
import com.clemble.casino.event.PlayerAwareEvent;
import com.clemble.casino.server.SystemPaymentTransactionRequestEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(value = {
    @JsonSubTypes.Type(name = SystemPlayerLeftEvent.CHANNEL, value = SystemPlayerLeftEvent.class),
    @JsonSubTypes.Type(name = SystemPlayerEnteredEvent.CHANNEL, value = SystemPlayerEnteredEvent.class),
    @JsonSubTypes.Type(name = SystemPlayerPresenceChangedEvent.CHANNEL, value = SystemPlayerPresenceChangedEvent.class),
    @JsonSubTypes.Type(name = SystemPlayerSocialAddedEvent.CHANNEL, value = SystemPlayerSocialAddedEvent.class),
    @JsonSubTypes.Type(name = SystemPlayerDiscoveredConnectionEvent.CHANNEL, value = SystemPlayerDiscoveredConnectionEvent.class),
    @JsonSubTypes.Type(name = SystemPlayerConnectionsFetchedEvent.CHANNEL, value = SystemPlayerConnectionsFetchedEvent.class),
    @JsonSubTypes.Type(name = SystemPlayerCreatedEvent.CHANNEL, value = SystemPlayerCreatedEvent.class),
    @JsonSubTypes.Type(name = SystemPlayerProfileRegistered.CHANNEL, value = SystemPlayerProfileRegistered.class),
    @JsonSubTypes.Type(name = SystemPlayerSocialGrantRegistered.CHANNEL, value = SystemPlayerSocialGrantRegistered.class),
    @JsonSubTypes.Type(name = SystemPlayerSocialRegistered.CHANNEL, value = SystemPlayerSocialRegistered.class),
    @JsonSubTypes.Type(name = SystemGameStartedEvent.CHANNEL, value = SystemGameStartedEvent.class),
    @JsonSubTypes.Type(name = SystemGameEndedEvent.CHANNEL, value = SystemGameEndedEvent.class),
    @JsonSubTypes.Type(name = SystemPaymentTransactionRequestEvent.CHANNEL, value = SystemPaymentTransactionRequestEvent.class)
})
public interface SystemEvent extends Event {

    @JsonIgnore
    public String getChannel();

}
