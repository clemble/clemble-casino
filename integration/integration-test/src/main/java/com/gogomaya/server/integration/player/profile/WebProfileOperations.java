package com.gogomaya.server.integration.player.profile;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.player.PlayerProfile;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.web.player.PlayerProfileController;

public class WebProfileOperations implements ProfileOperations {

    final private PlayerProfileController profileController;

    public WebProfileOperations(PlayerProfileController profileController) {
        this.profileController = checkNotNull(profileController);
    }

    @Override
    public PlayerProfile get(Player player, long playerId) {
        return profileController.getPlayerProfile(playerId);
    }

    @Override
    public PlayerProfile put(Player player, long playerId, PlayerProfile playerProfile) {
        return profileController.updatePlayerProfile(playerId, playerProfile);
    }

}
