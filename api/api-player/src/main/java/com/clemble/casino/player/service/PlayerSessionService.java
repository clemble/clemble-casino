package com.clemble.casino.player.service;

import com.clemble.casino.player.security.PlayerSession;

public interface PlayerSessionService {

    public PlayerSession create(String player);

    public PlayerSession refreshPlayerSession(String player, long sessionId);

    public void endPlayerSession(String player, long sessionId);

    public PlayerSession getPlayerSession(String player, long sessionId);

}
