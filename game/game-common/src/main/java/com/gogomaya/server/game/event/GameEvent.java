package com.gogomaya.server.game.event;

import com.gogomaya.server.event.SessionAwareEvent;
import com.gogomaya.server.event.impl.AbstractGogomayaEvent;

public class GameEvent<State> extends AbstractGogomayaEvent implements SessionAwareEvent {

    /**
     * Generated 07/05/13
     */
    private static final long serialVersionUID = -4837244615682915463L;

    private long session;

    private State state;

    @Override
    public long getSession() {
        return session;
    }

    public void setSession(long session) {
        this.session = session;
    }

    public State getState() {
        return state;
    }

    public GameEvent<State> setState(State state) {
        this.state = state;
        return this;
    }

}
