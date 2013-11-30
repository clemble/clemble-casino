package com.clemble.casino.game.event.server;

import java.io.Serializable;

import com.clemble.casino.event.ServerEvent;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.GameSessionAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

abstract public class GameServerEvent<State extends GameState> implements GameSessionAware, ServerEvent, Serializable {

    /**
     * Generated 07/05/13
     */
    private static final long serialVersionUID = -4837244615682915463L;

    final private GameSessionKey session;
    final private State state;

    public GameServerEvent(GameSession<State> session) {
        this.session = session.getSession();
        this.state = session.getState();
    }

    @JsonCreator
    public GameServerEvent(@JsonProperty("session") GameSessionKey sessionKey, @JsonProperty("state") State state) {
        this.session = sessionKey;
        this.state = state;
    }

    @Override
    public GameSessionKey getSession() {
        return session;
    }

    public State getState() {
        return state;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((session == null) ? 0 : session.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameServerEvent<?> other = (GameServerEvent<?>) obj;
        if (session == null) {
            if (other.session != null)
                return false;
        } else if (!session.equals(other.session))
            return false;
        if (state == null) {
            if (other.state != null)
                return false;
        } else if (!state.equals(other.state))
            return false;
        return true;
    }

}
