package com.clemble.casino.server.event.email;

import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 12/6/14.
 */
public class SystemEmailVerifiedEvent implements SystemEmailEvent {

    final public static String CHANNEL = "sys:email:verified";

    final private String player;

    @JsonCreator
    public SystemEmailVerifiedEvent(@JsonProperty(PLAYER) String player) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemEmailVerifiedEvent that = (SystemEmailVerifiedEvent) o;

        if (player != null ? !player.equals(that.player) : that.player != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return player != null ? player.hashCode() : 0;
    }

}
