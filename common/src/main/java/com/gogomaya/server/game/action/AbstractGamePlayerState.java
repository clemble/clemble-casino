package com.gogomaya.server.game.action;

public class AbstractGamePlayerState implements GamePlayerState {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -9093552628785671625L;

    final private long playerId;

    protected AbstractGamePlayerState(final long playerId) {
        this.playerId = playerId;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

}
