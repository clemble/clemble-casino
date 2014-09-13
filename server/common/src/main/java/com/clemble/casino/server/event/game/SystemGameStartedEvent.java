package com.clemble.casino.server.event.game;

import com.clemble.casino.game.construction.GameInitiation;
import com.clemble.casino.game.construction.GameInitiationAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 7/4/14.
 */
public class SystemGameStartedEvent implements SystemGameEvent, GameInitiationAware {

    final public static String CHANNEL = "game:started";

    final private String sessionKey;
    final private GameInitiation initiation;

    @JsonCreator
    public SystemGameStartedEvent(
        @JsonProperty(SESSION_KEY) String sessionKey,
        @JsonProperty("initiation") GameInitiation initiation) {
        this.sessionKey = sessionKey;
        this.initiation = initiation;
    }

    @Override
    public GameInitiation getInitiation() {
        return initiation;
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
