package com.clemble.casino.server.event.player;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.PlayerProfileAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 7/4/14.
 */
public class SystemPlayerProfileRegisteredEvent implements PlayerProfileAware, SystemPlayerRegisteredEvent {

    final public static String CHANNEL = "registered:profile";

    final private String player;
    final private PlayerProfile profile;

    @JsonCreator
    public SystemPlayerProfileRegisteredEvent(
        @JsonProperty(PLAYER) String player,
        @JsonProperty("playerProfile") PlayerProfile profile) {
        this.player = player;
        this.profile = profile;
    }

    @Override
    public PlayerProfile getPlayerProfile() {
        return profile;
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

        SystemPlayerProfileRegisteredEvent that = (SystemPlayerProfileRegisteredEvent) o;

        if (!player.equals(that.player)) return false;
        if (!profile.equals(that.profile)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + profile.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "sys:" + player +  ":" + CHANNEL;
    }

}
