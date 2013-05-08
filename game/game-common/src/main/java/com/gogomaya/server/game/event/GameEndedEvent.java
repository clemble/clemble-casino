package com.gogomaya.server.game.event;

import com.gogomaya.server.event.PlayerAwareEvent;
import com.gogomaya.server.game.action.GameState;

public class GameEndedEvent<State extends GameState> extends GameEvent<State> implements PlayerAwareEvent {

    /**
     * Generated 07/05/13
     */
    private static final long serialVersionUID = 820200145932972096L;

    private long playerId;

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public GameEndedEvent<State> setPlayerId(long playerId) {
        this.playerId = playerId;
        return this;
    }

}
