package com.gogomaya.client.player.service;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import com.gogomaya.player.security.PlayerSession;
import com.gogomaya.player.service.PlayerSessionService;

public class SimplePlayerSessionOperations implements PlayerSessionOperations {
    
    final private String player;
    final private PlayerSessionService playerSessionService;
    
    public SimplePlayerSessionOperations(String player, PlayerSessionService playerSessionService) {
        this.player = player;
        this.playerSessionService = checkNotNull(playerSessionService);
    }

    @Override
    public PlayerSession create() {
        return playerSessionService.create(player);
    }

    @Override
    public PlayerSession refreshPlayerSession(long sessionId) {
        return playerSessionService.refreshPlayerSession(player, sessionId);
    }

    @Override
    public PlayerSession endPlayerSession(long sessionId) {
        return playerSessionService.endPlayerSession(player, sessionId);
    }

    @Override
    public PlayerSession getPlayerSession(long sessionId) {
        return playerSessionService.getPlayerSession(player, sessionId);
    }

}
