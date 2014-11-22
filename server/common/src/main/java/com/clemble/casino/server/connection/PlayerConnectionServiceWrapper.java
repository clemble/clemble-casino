package com.clemble.casino.server.connection;

import com.clemble.casino.player.service.PlayerConnectionService;

import java.util.Set;

/**
 * Wrapper for PlayerConnectionService to prevent double initiation of PlayerConnectionServiceController in DispatcherServlet
 */
public class PlayerConnectionServiceWrapper implements PlayerConnectionService {

    final private PlayerConnectionService delegate;

    public PlayerConnectionServiceWrapper(PlayerConnectionService delegate) {
        this.delegate = delegate;
    }

    @Override
    public Set<String> getConnections(String player) {
        return delegate.getConnections(player);
    }

    @Override
    public Set<String> myConnections() {
        return delegate.myConnections();
    }
}
