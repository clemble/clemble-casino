package com.clemble.casino.server.event.game;

import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.server.event.SystemEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

/**
 * Created by mavarazy on 7/4/14.
 */
public class SystemGameEndedEvent implements SystemEvent, GameSessionAware {

    final public static String CHANNEL = "game:ended";

    final private GameSessionKey sessionKey;
    final private Collection<String> participants;

    @JsonCreator
    public SystemGameEndedEvent(@JsonProperty(GameSessionAware.SESSION_KEY) GameSessionKey sessionKey, @JsonProperty("participants") Collection<String> participants) {
        this.sessionKey = sessionKey;
        this.participants = participants;
    }

    @Override
    public GameSessionKey getSessionKey() {
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
