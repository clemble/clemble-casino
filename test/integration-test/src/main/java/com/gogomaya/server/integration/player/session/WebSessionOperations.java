package com.gogomaya.server.integration.player.session;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.player.security.PlayerSession;
import com.gogomaya.server.web.player.PlayerSessionController;

public class WebSessionOperations extends AbstractSessionOperations {

    final private PlayerSessionController playerSessionController;

    public WebSessionOperations(PlayerSessionController playerSessionController) {
        this.playerSessionController = checkNotNull(playerSessionController);
    }

    @Override
    public PlayerSession start(Player player) {
        return playerSessionController.post(player.getPlayerId(), player.getPlayerId());
    }

    @Override
    public PlayerSession end(Player player, long session) {
        return playerSessionController.end(player.getPlayerId(), player.getPlayerId(), session);
    }

    @Override
    public PlayerSession get(Player player, long session) {
        return playerSessionController.get(player.getPlayerId(), player.getPlayerId(), session);
    }

    @Override
    public PlayerSession refresh(Player player, long session) {
        return playerSessionController.refresh(player.getPlayerId(), player.getPlayerId(), session);
    }

    @Override
    public List<PlayerSession> list(Player player) {
        return playerSessionController.list(player.getPlayerId(), player.getPlayerId());
    }

}
