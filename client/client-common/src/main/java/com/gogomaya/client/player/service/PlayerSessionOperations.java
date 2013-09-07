package com.gogomaya.client.player.service;

import com.gogomaya.player.security.PlayerSession;

public interface PlayerSessionOperations {

    public PlayerSession create();

    public PlayerSession refreshPlayerSession(long sessionId);

    public PlayerSession endPlayerSession(long sessionId);

    public PlayerSession getPlayerSession(long sessionId);

}
