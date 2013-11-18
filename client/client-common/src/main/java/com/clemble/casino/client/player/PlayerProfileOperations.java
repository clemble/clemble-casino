package com.clemble.casino.client.player;

import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.PlayerProfile;

public interface PlayerProfileOperations extends PlayerAware {

    public <T extends PlayerProfile> T getPlayerProfile();
    
    public <T extends PlayerProfile> T getPlayerProfile(String player);

    public <T extends PlayerProfile> T updatePlayerProfile(PlayerProfile playerProfile);

}
