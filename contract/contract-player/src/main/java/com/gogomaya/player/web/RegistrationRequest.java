package com.gogomaya.player.web;

import com.gogomaya.player.PlayerProfile;
import com.gogomaya.player.security.PlayerCredential;

public class RegistrationRequest {

    private PlayerProfile playerProfile;

    private PlayerCredential playerCredential;

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public RegistrationRequest setPlayerProfile(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
        return this;
    }

    public PlayerCredential getPlayerCredential() {
        return playerCredential;
    }

    public RegistrationRequest setPlayerCredential(PlayerCredential playerCredential) {
        this.playerCredential = playerCredential;
        return this;
    }

}
