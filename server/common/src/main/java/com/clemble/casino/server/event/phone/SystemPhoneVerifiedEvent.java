package com.clemble.casino.server.event.phone;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 12/8/14.
 */
public class SystemPhoneVerifiedEvent implements SystemPhoneEvent {

    final public static String CHANNEL = "sys:phone:verified";

    final private String player;

    @JsonCreator
    public SystemPhoneVerifiedEvent(@JsonProperty(PLAYER) String player) {
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

        SystemPhoneVerifiedEvent that = (SystemPhoneVerifiedEvent) o;

        if (player != null ? !player.equals(that.player) : that.player != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return player != null ? player.hashCode() : 0;
    }

}
