package com.gogomaya.player.security;

import java.util.List;

public interface PlayerSessionService {

    public PlayerSession create(long playerId);

    public PlayerSession getPlayerSession(long playerId, long sessionId);

    public PlayerSession refresh(long playerId, long sessionId);

    public PlayerSession end(long playerId, long sessionId);

    public List<PlayerSession> list(long playerId);

}
