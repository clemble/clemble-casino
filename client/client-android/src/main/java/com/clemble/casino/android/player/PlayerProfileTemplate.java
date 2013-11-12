package com.clemble.casino.android.player;

import com.clemble.casino.client.player.PlayerProfileOperations;
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
    public PlayerProfile getPlayerProfile() {
        return playerProfileService.getPlayerProfile(player);
    }

    @Override
    public PlayerProfile getPlayerProfile(String player) {
        return playerProfileService.getPlayerProfile(player);
    }

    @Override
    public PlayerProfile updatePlayerProfile(PlayerProfile playerProfile) {
        playerProfile.setPlayer(player);
        return playerProfileService.updatePlayerProfile(player, playerProfile);
    }

}
