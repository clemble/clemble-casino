package com.clemble.casino.server.event;

import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.GameSessionKey;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

/**
 * Created by mavarazy on 7/4/14.
 */
public class SystemGameStartedEvent implements SystemEvent, GameSessionAware {

    final public static String CHANNEL = "game:started";

    final private GameSessionKey sessionKey;
    final private Collection<String> participants;

    @JsonCreator
    public SystemGameStartedEvent(
        @JsonProperty("session") GameSessionKey sessionKey,
        @JsonProperty("participants") Collection<String> participants) {
        this.sessionKey = sessionKey;
        this.participants = participants;
    }

    @Override
    public GameSessionKey getSession() {
        return sessionKey;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
