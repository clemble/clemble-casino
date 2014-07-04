package com.clemble.casino.server.event;

import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.player.SocialConnectionDataAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 7/4/14.
 */
public class SystemPlayerSocialRegistered
    extends SystemPlayerRegisteredEvent
    implements SocialConnectionDataAware {

    final private SocialConnectionData socialConnectionData;

    @JsonCreator
    public SystemPlayerSocialRegistered(@JsonProperty("player") String player,
        @JsonProperty("socialConnectionData") SocialConnectionData socialConnectionData) {
        super(player);
        this.socialConnectionData = socialConnectionData;
    }

    @Override
    public SocialConnectionData getSocialConnectionData() {
        return socialConnectionData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SystemPlayerSocialRegistered that = (SystemPlayerSocialRegistered) o;

        if (socialConnectionData != null ? !socialConnectionData.equals(that.socialConnectionData) : that.socialConnectionData != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (socialConnectionData != null ? socialConnectionData.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return super.toString() + ":social";
    }

}
