package com.clemble.casino.integration.player.listener;

import com.clemble.casino.player.security.PlayerSession;

public interface PlayerListenerOperations {

    public PlayerListenerControl listen(PlayerSession playerSession, PlayerListener listener);

    public PlayerListenerControl listen(PlayerSession playerSession, PlayerListener listener, ListenerChannel channel);

}
