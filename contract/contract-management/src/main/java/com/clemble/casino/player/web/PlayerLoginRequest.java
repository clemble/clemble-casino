package com.clemble.casino.player.web;

import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerIdentity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerLoginRequest {

    final private PlayerCredential playerCredential;

    final private PlayerIdentity playerIdentity;

    @JsonCreator
    public PlayerLoginRequest(@JsonProperty("playerIdentity") final PlayerIdentity playerIdentity, @JsonProperty("playerCredential") final PlayerCredential playerCredential) {
        this.playerCredential = playerCredential;
        this.playerIdentity = playerIdentity;
    }

    final public PlayerCredential getPlayerCredential() {
        return playerCredential;
    }

    final public PlayerIdentity getPlayerIdentity() {
        return playerIdentity;
    }

}
