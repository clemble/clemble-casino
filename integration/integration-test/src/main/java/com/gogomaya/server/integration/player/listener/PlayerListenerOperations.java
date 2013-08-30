package com.gogomaya.server.integration.player.listener;

import com.gogomaya.player.security.PlayerSession;

public interface PlayerListenerOperations {

    public PlayerListenerControl listen(PlayerSession playerSession, PlayerListener listener);

    public PlayerListenerControl listen(PlayerSession playerSession, PlayerListener listener, ListenerChannel channel);

}