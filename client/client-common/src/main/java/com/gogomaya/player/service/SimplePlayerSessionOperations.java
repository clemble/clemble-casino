package com.gogomaya.player.service;


import static com.gogomaya.utils.Preconditions.checkNotNull;

import java.util.List;

import com.gogomaya.player.security.PlayerSession;

public class SimplePlayerSessionOperations implements PlayerSessionOperations {

    /**
     * 
     */
    private static final long serialVersionUID = -6444593345118760229L;

    final private long playerId;
    final private PlayerSessionService playerSessionService;

    public SimplePlayerSessionOperations(long playerId, PlayerSessionService playerSessionService) {
        this.playerId = playerId;
        this.playerSessionService = checkNotNull(playerSessionService);
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    @Override
    public PlayerSession createPlayerSession() {
        return playerSessionService.create(playerId);
    }

    @Override
    public PlayerSession getPlayerSession(long sessionId) {
        return playerSessionService.getPlayerSession(playerId, sessionId);
    }

    @Override
    public List<PlayerSession> listPlayerSessions() {
        return playerSessionService.listPlayerSessions(playerId);
    }

    @Override
    public PlayerSession refreshPlayerSession(long sessionId) {
        return playerSessionService.refreshPlayerSession(playerId, sessionId);
    }

    @Override
    public PlayerSession endPlayerSession(long sessionId) {
        return playerSessionService.endPlayerSession(playerId, sessionId);
    }

}
