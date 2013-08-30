package com.gogomaya.game.event.client;

import com.gogomaya.event.ClientEvent;


abstract public class GameClientEvent implements ClientEvent {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = 5862534746429660030L;

    final private long playerId;

    public GameClientEvent(final long playerId) {
        this.playerId = playerId;
    }

    @Override
    final public long getPlayerId() {
        return playerId;
    }

    @Override
    public String toString() {
        return "GameMove [playerId=" + playerId + "]";
    }

    @Override
    public int hashCode() {
        return (int) (playerId ^ (playerId >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GameClientEvent && playerId == ((GameClientEvent) obj).getPlayerId();
    }

}
