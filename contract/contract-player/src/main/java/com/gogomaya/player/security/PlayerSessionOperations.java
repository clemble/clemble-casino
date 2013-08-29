package com.gogomaya.player.security;

import java.util.List;

public interface PlayerSessionOperations {

    public PlayerSession createPlayerSession();

    public PlayerSession getPlayerSession(long sessionId);

    public List<PlayerSession> listPlayerSessions();

    public PlayerSession updatePlayerSession(long sessionId);

    public PlayerSession terminatePlayerSession(long sessionId);

}
