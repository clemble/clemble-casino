package com.gogomaya.server.integration.player.profile;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.web.player.session.PlayerProfileController;

public class WebPlayerProfileOperations implements PlayerProfileOperations {

    final private PlayerProfileController profileController;

    public WebPlayerProfileOperations(PlayerProfileController profileController) {
        this.profileController = checkNotNull(profileController);
    }

    @Override
    public PlayerProfile get(Player player, long playerId) {
        return profileController.get(player.getPlayerId(), playerId);
    }

    @Override
    public PlayerProfile put(Player player, long playerId, PlayerProfile playerProfile) {
        return profileController.put(player.getPlayerId(), playerId, playerProfile);
    }

}
