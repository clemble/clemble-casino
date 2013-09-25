package com.gogomaya.game.event.server;

import java.io.Serializable;

import com.gogomaya.event.ServerEvent;
import com.gogomaya.game.GameSession;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.GameState;
import com.gogomaya.game.SessionAware;

abstract public class GameServerEvent<State extends GameState> implements SessionAware, ServerEvent, Serializable {

    /**
     * Generated 07/05/13
     */
    private static final long serialVersionUID = -4837244615682915463L;

    private GameSessionKey session;
    private State state;

    public GameServerEvent() {
    }

    public GameServerEvent(GameSession<State> session) {
        this.session = session.getSession();
        this.state = session.getState();
    }

    public GameServerEvent(SessionAware sessionAware) {
        this.session = sessionAware.getSession();
    }

    public GameServerEvent(GameSessionKey session) {
        this.session = session;
    }

    @Override
    public GameSessionKey getSession() {
        return session;
    }

    public GameServerEvent<State> setSession(GameSessionKey session) {
        this.session = session;
        return this;
    }

    public State getState() {
        return state;
    }

    public GameServerEvent<State> setState(State state) {
        this.state = state;
        return this;
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
