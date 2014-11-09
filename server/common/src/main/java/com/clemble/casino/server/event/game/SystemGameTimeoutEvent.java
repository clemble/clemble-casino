package com.clemble.casino.server.event.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 11/8/14.
 */
public class SystemGameTimeoutEvent implements SystemGameEvent {

    final public static String CHANNEL = "sys:game:timeout";

    final private String sessionKey;

    @JsonCreator
    public SystemGameTimeoutEvent(@JsonProperty(SESSION_KEY) String sessionKey) {
        this.sessionKey = sessionKey;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemGameTimeoutEvent that = (SystemGameTimeoutEvent) o;

        if (!sessionKey.equals(that.sessionKey)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return sessionKey.hashCode();
    }

    @Override
    public String toString() {
        return sessionKey + " > " + CHANNEL;
    }
}
