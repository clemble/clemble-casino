package com.gogomaya.client.player.service;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import com.gogomaya.player.security.PlayerSession;
import com.gogomaya.player.service.PlayerSessionService;

public class SimplePlayerSessionOperations implements PlayerSessionOperations {
    
    final private long playerId;
    final private PlayerSessionService playerSessionService;
    
    public SimplePlayerSessionOperations(long playerId, PlayerSessionService playerSessionService) {
        this.playerId = playerId;
        this.playerSessionService = checkNotNull(playerSessionService);
    }

    @Override
    public PlayerSession create() {
        return playerSessionService.create(playerId);
    }

    @Override
    public PlayerSession refreshPlayerSession(long sessionId) {
        return playerSessionService.refreshPlayerSession(playerId, sessionId);
    }

    @Override
    public PlayerSession endPlayerSession(long sessionId) {
        return playerSessionService.endPlayerSession(playerId, sessionId);
    }

    @Override
    public PlayerSession getPlayerSession(long sessionId) {
        return playerSessionService.getPlayerSession(playerId, sessionId);
    }

}
