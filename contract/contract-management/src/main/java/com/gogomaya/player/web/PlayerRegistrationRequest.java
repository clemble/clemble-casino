package com.gogomaya.player.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.player.PlayerProfile;
import com.gogomaya.player.security.PlayerCredential;
import com.gogomaya.player.security.PlayerIdentity;

public class PlayerRegistrationRequest extends PlayerLoginRequest {

    final private PlayerProfile playerProfile;

    @JsonCreator
    public PlayerRegistrationRequest(
            @JsonProperty("playerProfile") PlayerProfile playerProfile,
            @JsonProperty("playerCredential") PlayerCredential playerCredential,
            @JsonProperty("playerIdentity") PlayerIdentity playerIdentity) {
        super(playerIdentity, playerCredential);
        this.playerProfile = playerProfile;
    }

    final public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

}
