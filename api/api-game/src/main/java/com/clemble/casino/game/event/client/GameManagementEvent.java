package com.clemble.casino.game.event.client;

import com.clemble.casino.event.ClientEvent;

public class GameManagementEvent implements ClientEvent {

    /**
     * Generated 29/11/13
     */
    private static final long serialVersionUID = -5929491459681131145L;

    final private String player;

    protected GameManagementEvent(String player) {
        this.player = player;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public int hashCode() {
        return ((player == null) ? 0 : player.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameManagementEvent other = (GameManagementEvent) obj;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        return true;
    }

}
