package com.gogomaya.player.service;

import java.util.List;

import com.gogomaya.player.security.PlayerSession;

public interface PlayerSessionOperations {

    public PlayerSession createPlayerSession();

    public PlayerSession getPlayerSession(long sessionId);

    public List<PlayerSession> listPlayerSessions();

    public PlayerSession updatePlayerSession(long sessionId);

    public PlayerSession terminatePlayerSession(long sessionId);

}
