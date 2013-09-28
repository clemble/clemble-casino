package com.gogomaya.client.player.service;

import com.gogomaya.player.PlayerAware;
import com.gogomaya.player.PlayerProfile;

public interface PlayerProfileOperations extends PlayerAware {

    public PlayerProfile getPlayerProfile();
    
    public PlayerProfile getPlayerProfile(String player);

    public PlayerProfile updatePlayerProfile(PlayerProfile playerProfile);

}
