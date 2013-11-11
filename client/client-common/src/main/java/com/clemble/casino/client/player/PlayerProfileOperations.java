package com.clemble.casino.client.player;

import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.PlayerProfile;

public interface PlayerProfileOperations extends PlayerAware {

    public PlayerProfile getPlayerProfile();
    
    public PlayerProfile getPlayerProfile(String player);

    public PlayerProfile updatePlayerProfile(PlayerProfile playerProfile);

}
