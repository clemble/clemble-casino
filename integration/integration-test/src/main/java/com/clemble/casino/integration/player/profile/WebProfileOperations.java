package com.clemble.casino.integration.player.profile;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.integration.player.Player;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.server.web.player.PlayerProfileController;

public class WebProfileOperations implements ProfileOperations {

    final private PlayerProfileController profileController;

    public WebProfileOperations(PlayerProfileController profileController) {
        this.profileController = checkNotNull(profileController);
    }

    @Override
    public PlayerProfile get(Player player, String playerId) {
        return profileController.getPlayerProfile(playerId);
    }

    @Override
    public PlayerProfile put(Player player, String playerId, PlayerProfile playerProfile) {
        return profileController.updatePlayerProfile(playerId, playerProfile);
    }

}
