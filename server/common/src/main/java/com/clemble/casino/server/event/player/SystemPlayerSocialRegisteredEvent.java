package com.clemble.casino.server.event.player;

import com.clemble.casino.social.SocialConnectionData;
import com.clemble.casino.social.SocialConnectionDataAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 7/4/14.
 */
public class SystemPlayerSocialRegisteredEvent
    implements SocialConnectionDataAware,
        SystemPlayerRegisteredEvent {

    final public static String CHANNEL = "sys:registration:social";

    final private String player;
    final private SocialConnectionData socialConnectionData;

    @JsonCreator
    public SystemPlayerSocialRegisteredEvent(@JsonProperty(PLAYER) String player,
                                             @JsonProperty("socialConnectionData") SocialConnectionData socialConnectionData) {
        this.player = player;
        this.socialConnectionData = socialConnectionData;
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
    public SocialConnectionData getSocialConnectionData() {
        return socialConnectionData;
    }

    @Override
    public String toString() {
        return player + " > " + CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemPlayerSocialRegisteredEvent that = (SystemPlayerSocialRegisteredEvent) o;

        if (!player.equals(that.player)) return false;
        if (!socialConnectionData.equals(that.socialConnectionData)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + socialConnectionData.hashCode();
        return result;
    }

}
