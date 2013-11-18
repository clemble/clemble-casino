package com.clemble.casino.client.player;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.service.PlayerProfileService;

public class PlayerProfileTemplate implements PlayerProfileOperations {

    /**
     * 
     */
    private static final long serialVersionUID = 2044631083380608080L;

    final private String player;
    final private PlayerProfileService playerProfileService;

    public PlayerProfileTemplate(String player, PlayerProfileService playerProfileService) {
        this.player = player;
        this.playerProfileService = playerProfileService;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends PlayerProfile> T getPlayerProfile() {
        return (T) playerProfileService.getPlayerProfile(player);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends PlayerProfile> T getPlayerProfile(String player) {
        return (T) playerProfileService.getPlayerProfile(player);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends PlayerProfile> T updatePlayerProfile(PlayerProfile playerProfile) {
        playerProfile.setPlayer(player);
        return (T) playerProfileService.updatePlayerProfile(player, playerProfile);
    }

}
