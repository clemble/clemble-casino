package com.clemble.casino.server.event;

import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.player.Presence;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SystemPlayerPresenceChangedEvent implements SystemEvent, PlayerAware {

    /**
     * Generated 13/12/13
     */
    private static final long serialVersionUID = 6335808874731056541L;

    final public static String CHANNEL = "presence";

    final private String player;
    final private Presence presence;

    public SystemPlayerPresenceChangedEvent(PlayerPresence playerPresence) {
        this(playerPresence.getPlayer(), playerPresence.getPresence());
    }

    @JsonCreator
    public SystemPlayerPresenceChangedEvent(@JsonProperty("player") String player, @JsonProperty("presence") Presence presence) {
        this.player = player;
        this.presence = presence;
    }

    public Presence getPresence() {
        return presence;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public String getChannel(){
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
        SystemPlayerPresenceChangedEvent other = (SystemPlayerPresenceChangedEvent) obj;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "sys:" + player + ":" + CHANNEL + ":" + presence;
    }

}
