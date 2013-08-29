package com.gogomaya.player;

public interface PlayerProfileService {

    public PlayerProfile getPlayerProfile(long playerId);

    public PlayerProfile updatePlayerProfile(long playerId, PlayerProfile playerProfile);

}
