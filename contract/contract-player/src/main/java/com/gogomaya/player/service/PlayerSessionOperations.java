package com.gogomaya.player.service;

import java.util.List;

import com.gogomaya.player.PlayerAware;
import com.gogomaya.player.security.PlayerSession;

public interface PlayerSessionOperations extends PlayerAware {

    public PlayerSession createPlayerSession();

    public PlayerSession getPlayerSession(long sessionId);

    public List<PlayerSession> listPlayerSessions();

    public PlayerSession refreshPlayerSession(long sessionId);

    public PlayerSession endPlayerSession(long sessionId);

}
