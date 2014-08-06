package com.clemble.casino.integration.player;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.player.PlayerSession;
import com.clemble.casino.player.service.PlayerSessionService;
import com.clemble.casino.player.service.PlayerSessionServiceContract;

import com.clemble.casino.server.presence.controller.*;

public class IntegrationPlayerSessionService implements PlayerSessionService {

    final private String player;
    final private PlayerSessionServiceController playerSessionService;

    public IntegrationPlayerSessionService(String player, PlayerSessionServiceController playerSessionService) {
        this.player = player;
        this.playerSessionService = checkNotNull(playerSessionService);
    }

    @Override
    public PlayerSession create() {
        return playerSessionService.create(player);
    }

    @Override
    public PlayerSession refreshPlayerSession(String sessionId) {
        return playerSessionService.refreshPlayerSession(player, sessionId);
    }

    @Override
    public void endPlayerSession(String sessionId) {
        playerSessionService.endPlayerSession(player, sessionId);
    }

    @Override
    public PlayerSession getPlayerSession(String sessionId) {
        return playerSessionService.getPlayerSession(player, sessionId);
    }

}
