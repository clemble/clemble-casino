package com.clemble.casino.player.service;

import com.clemble.casino.player.PlayerProfile;

public interface PlayerProfileService {

    public PlayerProfile getPlayerProfile(String player);

    public PlayerProfile updatePlayerProfile(String player, PlayerProfile playerProfile);

}
