package com.clemble.casino.integration.player.session;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.integration.player.Player;
import com.clemble.casino.player.security.PlayerSession;
import com.clemble.casino.server.web.management.PlayerSessionController;

public class WebSessionOperations extends AbstractSessionOperations {

    final private PlayerSessionController playerSessionController;

    public WebSessionOperations(PlayerSessionController playerSessionController) {
        this.playerSessionController = checkNotNull(playerSessionController);
    }

    @Override
    public PlayerSession start(Player player) {
        return playerSessionController.create(player.getPlayer());
    }

    @Override
    public PlayerSession end(Player player, long session) {
        return playerSessionController.endPlayerSession(player.getPlayer(), session);
    }

    @Override
    public PlayerSession refresh(Player player, long session) {
        return playerSessionController.refreshPlayerSession(player.getPlayer(), session);
    }

    @Override
    public PlayerSession get(Player player, long session) {
        return playerSessionController.getPlayerSession(player.getPlayer(), session);
    }

}
