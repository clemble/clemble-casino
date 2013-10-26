package com.clemble.casino.game.event.client;

import com.clemble.casino.event.ClientEvent;


abstract public class GameClientEvent implements ClientEvent {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = 5862534746429660030L;

    final private String player;

    public GameClientEvent(final String player) {
        this.player = player;
    }

    @Override
    final public String getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "GameMove [playerId=" + player + "]";
    }

    @Override
    public int hashCode() {
        return player == null ? 0 : player.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GameClientEvent && player.equals(((GameClientEvent) obj).getPlayer());
    }

}
