package com.clemble.casino.server.event;

import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.GameSessionKey;

import java.util.Collection;

/**
 * Created by mavarazy on 7/4/14.
 */
public class SystemGameEndedEvent implements SystemEvent, GameSessionAware {

    final public static String CHANNEL = "game:ended";

    final private GameSessionKey sessionKey;
    final private Collection<String> participants;

    public SystemGameEndedEvent(GameSessionKey sessionKey, Collection<String> participants) {
        this.sessionKey = sessionKey;
        this.participants = participants;
    }

    @Override
    public GameSessionKey getSession() {
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
