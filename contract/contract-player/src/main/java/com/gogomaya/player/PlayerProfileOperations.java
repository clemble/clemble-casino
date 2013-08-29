package com.gogomaya.player;

public interface PlayerProfileOperations extends PlayerProfileService {

    public PlayerProfile getPlayerProfile();

    public PlayerProfile updatePlayerProfile(PlayerProfile playerProfile);

}
