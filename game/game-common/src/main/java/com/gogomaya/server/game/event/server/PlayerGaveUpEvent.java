package com.gogomaya.server.game.event.server;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.player.PlayerAware;

public class PlayerGaveUpEvent<State extends GameState> extends GameEvent<State> implements PlayerAware {

    /**
     * Generated 07/05/13
     */
    private static final long serialVersionUID = 8613548852525073195L;

    private long playerId;

    public PlayerGaveUpEvent() {
    }

    public PlayerGaveUpEvent(SessionAware sessionAware) {
        super(sessionAware);
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public PlayerGaveUpEvent<State> setPlayerId(long newPlayerId) {
        this.playerId = newPlayerId;
        return this;
    }

}
