package com.clemble.casino.server.event;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.PlayerProfileAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 7/4/14.
 */
public class SystemPlayerProfileRegistered
    extends SystemPlayerRegisteredEvent
    implements PlayerProfileAware {

    final private PlayerProfile profile;

    @JsonCreator
    public SystemPlayerProfileRegistered(
        @JsonProperty("player") String player,
        @JsonProperty("playerProfile") PlayerProfile profile) {
        super(player);
        this.profile = profile;
    }

    public PlayerProfile getPlayerProfile() {
        return profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SystemPlayerProfileRegistered that = (SystemPlayerProfileRegistered) o;

        if (profile != null ? !profile.equals(that.profile) : that.profile != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (profile != null ? profile.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return super.toString() + ":profile";
    }

}
