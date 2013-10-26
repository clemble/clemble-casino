package com.clemble.casino.player.web;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerIdentity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
