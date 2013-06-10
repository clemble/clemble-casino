package com.gogomaya.server.game.event.server;

import com.gogomaya.server.event.AbstractServerEvent;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.player.PlayerAware;

public class PlayerAddedEvent extends AbstractServerEvent implements PlayerAware, SessionAware {

    /**
     * Generated 14/05/13
     */
    private static final long serialVersionUID = 2164857037535224959L;

    private long playerId;

    private long sessionId;

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public PlayerAddedEvent setPlayerId(long playerId) {
        this.playerId = playerId;
        return this;
    }

    @Override
    public long getSession() {
        return sessionId;
    }

    public PlayerAddedEvent setSession(long sessionId) {
        this.sessionId = sessionId;
        return this;
    }

}
