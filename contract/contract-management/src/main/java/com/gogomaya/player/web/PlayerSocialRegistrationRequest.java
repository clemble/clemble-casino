package com.gogomaya.player.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.player.SocialConnectionData;
import com.gogomaya.player.security.PlayerCredential;
import com.gogomaya.player.security.PlayerIdentity;

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
