package com.clemble.casino.player.web;

import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.player.client.ClembleConsumerDetails;
import com.clemble.casino.player.security.PlayerCredential;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerSocialRegistrationRequest extends PlayerLoginRequest {

    private static final long serialVersionUID = 709897454794810031L;

    final private SocialConnectionData socialConnectionData;

    @JsonCreator
    public PlayerSocialRegistrationRequest(@JsonProperty("consumerDetails") ClembleConsumerDetails consumerDetails,
            @JsonProperty("playerCredential") PlayerCredential playerCredential, @JsonProperty("socialConnectionData") SocialConnectionData socialConnectionData) {
        super(consumerDetails, playerCredential);
        this.socialConnectionData = socialConnectionData;
    }

    public SocialConnectionData getSocialConnectionData() {
        return socialConnectionData;
    }

}
