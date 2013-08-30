package com.gogomaya.player.service;

import com.gogomaya.player.PlayerProfile;

public interface PlayerProfileService {

    public PlayerProfile getPlayerProfile(long playerId);

    public PlayerProfile updatePlayerProfile(long playerId, PlayerProfile playerProfile);

}
