package com.clemble.casino.server.event;

import com.clemble.casino.event.Event;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(name = SystemPlayerEnteredEvent.CHANNEL, value = SystemPlayerEnteredEvent.class),
        @JsonSubTypes.Type(name = SystemPlayerLeftEvent.CHANNEL, value = SystemPlayerLeftEvent.class),
        @JsonSubTypes.Type(name = SystemPlayerPresenceChangedEvent.CHANNEL, value = SystemPlayerPresenceChangedEvent.class),
        @JsonSubTypes.Type(name = SystemPlayerConnectedSocialEvent.CHANNEL, value = SystemPlayerConnectedSocialEvent.class),
        @JsonSubTypes.Type(name = SystemPlayerConnectionDiscoveredEvent.CHANNEL, value = SystemPlayerConnectionDiscoveredEvent.class),
        @JsonSubTypes.Type(name = SystemPlayerRegisteredEvent.CHANNEL, value = SystemPlayerRegisteredEvent.class)
})
public interface SystemEvent extends Event {

    @JsonIgnore
    public String getChannel();

}
