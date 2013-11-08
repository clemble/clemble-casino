package com.clemble.casino.player.web;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.client.ClembleConsumerDetails;
import com.clemble.casino.player.security.PlayerCredential;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerRegistrationRequest extends PlayerLoginRequest {

    private static final long serialVersionUID = -7419091879616342442L;

    final private PlayerProfile playerProfile;

    @JsonCreator
    public PlayerRegistrationRequest(
            @JsonProperty("playerProfile") PlayerProfile playerProfile,
            @JsonProperty("playerCredential") PlayerCredential playerCredential,
            @JsonProperty("consumerDetails") ClembleConsumerDetails consumerDetails) {
        super(consumerDetails, playerCredential);
        this.playerProfile = playerProfile;
    }

    final public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

}
