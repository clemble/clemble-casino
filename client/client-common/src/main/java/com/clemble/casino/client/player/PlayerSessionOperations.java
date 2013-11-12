package com.clemble.casino.client.player;

import com.clemble.casino.player.security.PlayerSession;

public interface PlayerSessionOperations {

    public PlayerSession create();

    public PlayerSession refreshPlayerSession(long sessionId);

    public void endPlayerSession(long sessionId);

    public PlayerSession getPlayerSession(long sessionId);

}
