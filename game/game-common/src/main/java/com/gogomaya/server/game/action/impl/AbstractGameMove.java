package com.gogomaya.server.game.action.impl;

import com.gogomaya.server.game.action.move.GameMove;

public class AbstractGameMove implements GameMove {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = 5862534746429660030L;

    final private long playerId;
    final private int moveId;

    public AbstractGameMove(final int moveId, final long playerId) {
        this.playerId = playerId;
        this.moveId = moveId;
    }

    @Override
    final public long getPlayerId() {
        return playerId;
    }

    @Override
    public int getMoveId() {
        return moveId;
    }

    @Override
    public String toString() {
        return "GameMove [playerId=" + playerId + ", moveId=" + moveId + "]";
    }

}
