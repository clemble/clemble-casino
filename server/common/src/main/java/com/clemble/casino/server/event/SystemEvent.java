package com.clemble.casino.server.event;

import com.clemble.casino.event.Event;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(name = "entered", value = PlayerEnteredEvent.class),
        @JsonSubTypes.Type(name = "left", value = PlayerLeftEvent.class),
        @JsonSubTypes.Type(name = "presenceChanged", value = SystemPlayerPresenceChangedEvent.class)
})
public interface SystemEvent extends Event {

}
