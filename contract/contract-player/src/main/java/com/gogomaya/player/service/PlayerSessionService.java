package com.gogomaya.player.service;

import com.gogomaya.player.security.PlayerSession;

public interface PlayerSessionService {

    public PlayerSession create(String player);

    public PlayerSession refreshPlayerSession(String player, long sessionId);

    public PlayerSession endPlayerSession(String player, long sessionId);

    public PlayerSession getPlayerSession(String player, long sessionId);

}
