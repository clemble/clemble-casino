package com.gogomaya.player.service;

import com.gogomaya.player.PlayerProfile;

public class SimplePlayerProfileOperations implements PlayerProfileOperations {

    /**
     * 
     */
    private static final long serialVersionUID = 2044631083380608080L;

    final private long playerId;
    final private PlayerProfileService playerProfileService;

    public SimplePlayerProfileOperations(long playerId, PlayerProfileService playerProfileService) {
        this.playerId = playerId;
        this.playerProfileService = playerProfileService;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    @Override
    public PlayerProfile getPlayerProfile() {
        return playerProfileService.getPlayerProfile(playerId);
    }

    @Override
    public PlayerProfile getPlayerProfile(long playerId) {
        return playerProfileService.getPlayerProfile(playerId);
    }

    @Override
    public PlayerProfile updatePlayerProfile(PlayerProfile playerProfile) {
        playerProfile.setPlayerId(playerId);
        return playerProfileService.updatePlayerProfile(playerId, playerProfile);
    }

}
