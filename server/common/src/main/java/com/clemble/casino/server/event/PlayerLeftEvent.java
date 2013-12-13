package com.clemble.casino.server.event;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerLeftEvent implements SystemEvent, PlayerAware {

    /**
     * Generated 13/11/13
     */
    private static final long serialVersionUID = -7854654395683761302L;

    final private String player;

    @JsonCreator
    public PlayerLeftEvent(@JsonProperty("player") String player) {
        this.player = checkNotNull(player);
    }

    @Override
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
        PlayerLeftEvent other = (PlayerLeftEvent) obj;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        return true;
    }
}
