package com.clemble.casino.server.event.player;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SystemPlayerLeftEvent implements SystemPlayerEvent {

    /**
     * Generated 13/11/13
     */
    private static final long serialVersionUID = -7854654395683761302L;

    final public static String CHANNEL = "sys:player:left";

    final private String player;

    @JsonCreator
    public SystemPlayerLeftEvent(@JsonProperty(PLAYER) String player) {
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
        SystemPlayerLeftEvent other = (SystemPlayerLeftEvent) obj;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        return true;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public String toString() {
        return player + " > " + CHANNEL;
    }
}
