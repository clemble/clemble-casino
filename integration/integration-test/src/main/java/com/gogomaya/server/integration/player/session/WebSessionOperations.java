package com.gogomaya.server.integration.player.session;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.player.security.PlayerSession;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.web.management.PlayerSessionController;

public class WebSessionOperations extends AbstractSessionOperations {

    final private PlayerSessionController playerSessionController;

    public WebSessionOperations(PlayerSessionController playerSessionController) {
        this.playerSessionController = checkNotNull(playerSessionController);
    }

    @Override
    public PlayerSession start(Player player) {
        return playerSessionController.create(player.getPlayerId());
    }

    @Override
    public PlayerSession end(Player player, long session) {
        return playerSessionController.endPlayerSession(player.getPlayerId(), session);
    }

    @Override
    public PlayerSession refresh(Player player, long session) {
        return playerSessionController.refreshPlayerSession(player.getPlayerId(), session);
    }

    @Override
    public PlayerSession get(Player player, long session) {
        return playerSessionController.getPlayerSession(player.getPlayerId(), session);
    }

}
