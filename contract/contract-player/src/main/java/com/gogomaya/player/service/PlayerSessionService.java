package com.gogomaya.player.service;

import java.util.List;

import com.gogomaya.player.security.PlayerSession;

public interface PlayerSessionService {

    public PlayerSession create(long playerId);

    public PlayerSession getPlayerSession(long playerId, long sessionId);

    public PlayerSession refresh(long playerId, long sessionId);

    public PlayerSession end(long playerId, long sessionId);

    public List<PlayerSession> list(long playerId);

}
