package com.clemble.casino.server.event.player;

import com.clemble.casino.social.SocialAccessGrant;
import com.clemble.casino.social.SocialAccessGrantAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 7/4/14.
 */
public class SystemPlayerSocialGrantRegisteredEvent
    implements SocialAccessGrantAware,
        SystemPlayerRegisteredEvent {

    final public static String CHANNEL = "registered:social:grant";

    final private String player;
    final private SocialAccessGrant socialGrant;

    @JsonCreator
    public SystemPlayerSocialGrantRegisteredEvent(@JsonProperty(PLAYER) String player, @JsonProperty("accessGrant") SocialAccessGrant socialGrant) {
        this.player = player;
        this.socialGrant = socialGrant;
    }

    @Override
    public SocialAccessGrant getAccessGrant() {
        return socialGrant;
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
    public String toString() {
        return "sys:" + player + ":" + CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemPlayerSocialGrantRegisteredEvent that = (SystemPlayerSocialGrantRegisteredEvent) o;

        if (!player.equals(that.player)) return false;
        if (!socialGrant.equals(that.socialGrant)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + socialGrant.hashCode();
        return result;
    }

}
