package com.clemble.casino.server.event;

import com.clemble.casino.event.PlayerAwareEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SystemPlayerRegisteredEvent implements SystemEvent, PlayerAwareEvent {

    /**
     * Generated 07/01/14
     */
    private static final long serialVersionUID = 7637036341950271549L;

    final public static String CHANNEL = "registered";

    final private String player;

    @JsonCreator
    public SystemPlayerRegisteredEvent(@JsonProperty("player") String player) {
        this.player = player;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
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
        SystemPlayerRegisteredEvent other = (SystemPlayerRegisteredEvent) obj;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "sys:registered:" + player;
    }

}
