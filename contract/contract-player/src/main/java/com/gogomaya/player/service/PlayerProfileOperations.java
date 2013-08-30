package com.gogomaya.player.service;

import com.gogomaya.player.PlayerProfile;

public interface PlayerProfileOperations extends PlayerProfileService {

    public PlayerProfile getPlayerProfile();

    public PlayerProfile updatePlayerProfile(PlayerProfile playerProfile);

}
