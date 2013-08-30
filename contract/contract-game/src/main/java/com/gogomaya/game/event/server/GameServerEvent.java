package com.gogomaya.game.event.server;

import java.io.Serializable;

import com.gogomaya.event.ServerEvent;
import com.gogomaya.game.GameSession;
import com.gogomaya.game.GameState;
import com.gogomaya.game.SessionAware;

abstract public class GameServerEvent<State extends GameState> implements SessionAware, ServerEvent, Serializable {

    /**
     * Generated 07/05/13
     */
    private static final long serialVersionUID = -4837244615682915463L;

    private long session;

    private State state;

    public GameServerEvent() {
    }

    public GameServerEvent(GameSession<State> session) {
        this.session = session.getSession();
        this.state = session.getState();
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
