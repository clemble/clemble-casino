package com.clemble.casino.server.event;

import com.clemble.casino.event.Event;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(name = "entered", value = SystemPlayerEnteredEvent.class),
        @JsonSubTypes.Type(name = "left", value = SystemPlayerLeftEvent.class),
        @JsonSubTypes.Type(name = "presenceChanged", value = SystemPlayerPresenceChangedEvent.class),
        @JsonSubTypes.Type(name = "socialRegistegered", value = SystemPlayerConnectedSocialEvent.class),
        @JsonSubTypes.Type(name = "discovered", value = SystemPlayerConnectionDiscoveredEvent.class)
})
public interface SystemEvent extends Event {

    @JsonIgnore
    public String getChannel();

}
