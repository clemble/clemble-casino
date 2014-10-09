package com.clemble.casino.server.event.player;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SystemPlayerEnteredEvent implements SystemPlayerEvent {

    /**
     * Generated 13/12/13
     */
    private static final long serialVersionUID = -2179848922729269755L;

    final public static String CHANNEL = "sys:player:entered";

    final private String player;

    @JsonCreator
    public SystemPlayerEnteredEvent(@JsonProperty(PLAYER) String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public String toString() {
        return player + " > " + CHANNEL;
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
        SystemPlayerEnteredEvent other = (SystemPlayerEnteredEvent) obj;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        return true;
    }

}
