package com.clemble.casino.server.event;

import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SystemPlayerDiscoveredConnectionEvent implements PlayerAware, SystemEvent {

    final public static String CHANNEL = "discovered";

    /**
     * Generated 06/01/14
     */
    private static final long serialVersionUID = 6999213345285403696L;

    final private String player;
    final private String discovered;

    @JsonCreator
    public SystemPlayerDiscoveredConnectionEvent(@JsonProperty("player") String player, @JsonProperty("discovered") String discovered) {
        this.player = player;
        this.discovered = discovered;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public String getDiscovered() {
        return discovered;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((discovered == null) ? 0 : discovered.hashCode());
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
        SystemPlayerDiscoveredConnectionEvent other = (SystemPlayerDiscoveredConnectionEvent) obj;
        if (discovered == null) {
            if (other.discovered != null)
                return false;
        } else if (!discovered.equals(other.discovered))
            return false;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "sys:discovered:" + player + ":" + discovered;
    }

}
