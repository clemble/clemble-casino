package com.clemble.casino.server.event.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

/**
 * Created by mavarazy on 7/4/14.
 */
public class SystemGameEndedEvent implements SystemGameEvent {

    final public static String CHANNEL = "game:ended";

    final private String sessionKey;
    final private Collection<String> participants;

    @JsonCreator
    public SystemGameEndedEvent(@JsonProperty(SESSION_KEY) String sessionKey, @JsonProperty("participants") Collection<String> participants) {
        this.sessionKey = sessionKey;
        this.participants = participants;
    }

    @Override
    public String getSessionKey() {
        return sessionKey;
    }

    public Collection<String> getParticipants() {
        return participants;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
