package com.clemble.casino.server.event.game;

import com.clemble.casino.game.construction.GameInitiation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 8/31/14.
 */
public class SystemGameReadyEvent implements SystemGameEvent {

    final public static String CHANNEL = "sys:game:ready";

    final private String sessionKey;
    final private GameInitiation initiation;

    public SystemGameReadyEvent(GameInitiation initiation) {
        this.sessionKey = initiation.getSessionKey();
        this.initiation = initiation;
    }

    @JsonCreator
    public SystemGameReadyEvent(@JsonProperty("sessionKey") String sessionKey, @JsonProperty("initiation") GameInitiation initiation) {
        this.sessionKey = sessionKey;
        this.initiation = initiation;
    }

    @Override
    public String getSessionKey() {
        return sessionKey;
    }

    public GameInitiation getInitiation() {
        return initiation;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemGameReadyEvent that = (SystemGameReadyEvent) o;

        if (initiation != null ? !initiation.equals(that.initiation) : that.initiation != null) return false;
        if (sessionKey != null ? !sessionKey.equals(that.sessionKey) : that.sessionKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sessionKey != null ? sessionKey.hashCode() : 0;
        result = 31 * result + (initiation != null ? initiation.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "sys:" + sessionKey +  ":" + CHANNEL;
    }

}
