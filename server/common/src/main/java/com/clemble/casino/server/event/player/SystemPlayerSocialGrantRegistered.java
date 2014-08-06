package com.clemble.casino.server.event.player;

import com.clemble.casino.social.SocialAccessGrant;
import com.clemble.casino.social.SocialAccessGrantAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 7/4/14.
 */
public class SystemPlayerSocialGrantRegistered
    implements SocialAccessGrantAware,
        SystemPlayerRegisteredEvent {

    final public static String CHANNEL = "registered:social:grant";

    final private String player;
    final private SocialAccessGrant socialGrant;

    @JsonCreator
    public SystemPlayerSocialGrantRegistered(@JsonProperty("player") String player, @JsonProperty("accessGrant") SocialAccessGrant socialGrant) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SystemPlayerSocialGrantRegistered that = (SystemPlayerSocialGrantRegistered) o;

        if (socialGrant != null ? !socialGrant.equals(that.socialGrant) : that.socialGrant != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (socialGrant != null ? socialGrant.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "sys:" + player + ":" + CHANNEL;
    }
}
