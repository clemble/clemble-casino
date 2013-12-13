package com.clemble.casino.server.event;

import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerEnteredEvent implements SystemEvent, PlayerAware {

    /**
     * Generated 13/12/13
     */
    private static final long serialVersionUID = -2179848922729269755L;

    final private String player;

    @JsonCreator
    public PlayerEnteredEvent(@JsonProperty("player") String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((player == null) ? 0 : player.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlayerEnteredEvent other = (PlayerEnteredEvent) obj;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        return true;
    }

}
