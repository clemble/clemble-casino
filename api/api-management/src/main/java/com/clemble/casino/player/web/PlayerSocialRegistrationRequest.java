package com.clemble.casino.player.web;

import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerIdentity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.clemble.casino.player.SocialConnectionData;

public class PlayerSocialRegistrationRequest extends PlayerLoginRequest {

    final private SocialConnectionData socialConnectionData;

    @JsonCreator
    public PlayerSocialRegistrationRequest(
            @JsonProperty("playerIdentity") PlayerIdentity playerIdentity,
            @JsonProperty("playerCredential") PlayerCredential playerCredential,
            @JsonProperty("socialConnectionData") SocialConnectionData socialConnectionData) {
        super(playerIdentity, playerCredential);
        this.socialConnectionData = socialConnectionData;
    }

    public SocialConnectionData getSocialConnectionData() {
        return socialConnectionData;
    }

}
