package com.clemble.casino.server.event.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

/**
 * Created by mavarazy on 7/4/14.
 */
public class SystemGameEndedEvent implements SystemGameEvent {

    final public static String CHANNEL = "sys:game:ended";

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

    @Override
    public String toString() {
        return sessionKey +  " > " + CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemGameEndedEvent that = (SystemGameEndedEvent) o;

        if (!participants.equals(that.participants)) return false;
        if (!sessionKey.equals(that.sessionKey)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sessionKey.hashCode();
        result = 31 * result + participants.hashCode();
        return result;
    }

}
