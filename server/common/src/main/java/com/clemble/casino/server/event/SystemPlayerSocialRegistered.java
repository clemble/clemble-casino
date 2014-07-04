package com.clemble.casino.server.event;

import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.player.SocialConnectionDataAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 7/4/14.
 */
public class SystemPlayerSocialRegistered
    implements SocialConnectionDataAware,
        SystemPlayerRegisteredEvent {

    final public static String CHANNEL = "registration:social";

    final private String player;
    final private SocialConnectionData socialConnectionData;

    @JsonCreator
    public SystemPlayerSocialRegistered(@JsonProperty("player") String player,
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
        return "sys:" + player + ":" + CHANNEL;
    }

}
