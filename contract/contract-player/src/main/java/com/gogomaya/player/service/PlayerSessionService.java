package com.gogomaya.player.service;

import com.gogomaya.player.security.PlayerSession;

public interface PlayerSessionService {

    public PlayerSession create(long playerId);

    public PlayerSession refreshPlayerSession(long playerId, long sessionId);

    public PlayerSession endPlayerSession(long playerId, long sessionId);

    public PlayerSession getPlayerSession(long playerId, long sessionId);

}
