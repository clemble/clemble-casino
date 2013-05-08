package com.gogomaya.server.game.event;

import com.gogomaya.server.event.PlayerAwareEvent;
import com.gogomaya.server.game.action.GameState;

public class PlayerGaveUpEvent<State extends GameState> extends GameEvent<State> implements PlayerAwareEvent {

    /**
     * Generated 07/05/13
     */
    private static final long serialVersionUID = 8613548852525073195L;

    private long playerId;

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public PlayerGaveUpEvent<State> setPlayerId(long newPlayerId) {
        this.playerId = newPlayerId;
        return this;
    }

}
