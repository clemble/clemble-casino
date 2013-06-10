package com.gogomaya.server.game.event.server;

import com.gogomaya.server.game.GameState;

abstract public class AbstractTimeBreachEvent<State extends GameState> extends GameEvent<State> {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = -5874059254722415487L;

    private long playerId;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

}
