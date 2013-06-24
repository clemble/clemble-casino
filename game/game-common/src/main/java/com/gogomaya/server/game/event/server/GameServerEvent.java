package com.gogomaya.server.game.event.server;

import java.io.Serializable;

import com.gogomaya.server.event.ServerEvent;
import com.gogomaya.server.game.SessionAware;

abstract public class GameServerEvent<State> implements SessionAware, ServerEvent, Serializable {

    /**
     * Generated 07/05/13
     */
    private static final long serialVersionUID = -4837244615682915463L;

    private long session;

    private State state;

    public GameServerEvent() {
    }

    public GameServerEvent(SessionAware sessionAware) {
        this.setSession(sessionAware.getSession());
    }

    @Override
    public long getSession() {
        return session;
    }

    public GameServerEvent<State> setSession(long session) {
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

}
