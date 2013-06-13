package com.gogomaya.server.game.event.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.game.GameState;

public class GameCanceled<State extends GameState> implements ServerConstructionEvent {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = 1L;

    final private long session;

    @JsonCreator
    public GameCanceled(@JsonProperty("session") long session) {
        this.session = session;
    }

    @Override
    public long getSession() {
        return session;
    }

}
