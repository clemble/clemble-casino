package com.gogomaya.server.player.web;

import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.security.PlayerCredential;

public class RegistrationRequest {

    private PlayerProfile playerProfile;

    private PlayerCredential playerCredential;

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public void setPlayerProfile(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    public PlayerCredential getPlayerCredential() {
        return playerCredential;
    }

    public void setPlayerCredential(PlayerCredential playerCredential) {
        this.playerCredential = playerCredential;
    }

}
