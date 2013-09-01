package com.gogomaya.client.player.service;

import com.gogomaya.player.PlayerAware;
import com.gogomaya.player.PlayerProfile;

public interface PlayerProfileOperations extends PlayerAware {

    public PlayerProfile getPlayerProfile();
    
    public PlayerProfile getPlayerProfile(long playerId);

    public PlayerProfile updatePlayerProfile(PlayerProfile playerProfile);

}
