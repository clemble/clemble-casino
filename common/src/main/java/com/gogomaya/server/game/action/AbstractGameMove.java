package com.gogomaya.server.game.action;

public class AbstractGameMove implements GameMove {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = 5862534746429660030L;

    final private long playerId;

    public AbstractGameMove(final long playerId) {
        this.playerId = playerId;
    }

    @Override
    final public long getPlayerId() {
        return playerId;
    }

}
