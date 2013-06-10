package com.gogomaya.server.game.event.server;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.game.event.client.SurrenderEvent;
import com.gogomaya.server.player.PlayerAware;

public class PlayerLostEvent<State extends GameState> extends GameEvent<State> implements PlayerAware {

    /**
     * Generated 07/05/13
     */
    private static final long serialVersionUID = 8613548852525073195L;

    private long playerId;

    private SurrenderEvent reason;

    public PlayerLostEvent() {
    }

    public PlayerLostEvent(SessionAware sessionAware) {
        super(sessionAware);
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public PlayerLostEvent<State> setPlayerId(long newPlayerId) {
        this.playerId = newPlayerId;
        return this;
    }

    public SurrenderEvent getReason() {
        return reason;
    }

    public PlayerLostEvent<State> setReason(SurrenderEvent reason) {
        this.reason = reason;
        return this;
    }

}
