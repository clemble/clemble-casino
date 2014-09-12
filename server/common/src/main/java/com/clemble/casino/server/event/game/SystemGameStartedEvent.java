package com.clemble.casino.server.event.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

/**
 * Created by mavarazy on 7/4/14.
 */
public class SystemGameStartedEvent implements SystemGameEvent {

    final public static String CHANNEL = "game:started";

    final private String sessionKey;
    final private Collection<String> participants;

    @JsonCreator
    public SystemGameStartedEvent(
        @JsonProperty(SESSION_KEY) String sessionKey,
        @JsonProperty("participants") Collection<String> participants) {
        this.sessionKey = sessionKey;
        this.participants = participants;
    }

    @Override
    public String getSessionKey() {
        return sessionKey;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public String toString() {
        return "sys:" + sessionKey +  ":" + CHANNEL;
    }

}
