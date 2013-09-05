package com.gogomaya.player.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.player.security.PlayerCredential;
import com.gogomaya.player.security.PlayerIdentity;

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
