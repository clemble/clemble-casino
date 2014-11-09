package com.clemble.casino.server.connection;

import com.clemble.casino.player.PlayerConnections;
import com.clemble.casino.player.service.PlayerConnectionService;
import org.springframework.social.connect.ConnectionKey;

import java.util.Set;

/**
 * Created by mavarazy on 11/9/14.
 */
public class ServerPlayerConnectionClient implements PlayerConnectionService {

    @Override
    public PlayerConnections getConnections(String player) {
        return null;
    }

    @Override
    public Set<ConnectionKey> getOwnedConnections(String player) {
        return null;
    }

    @Override
    public Set<String> getConnectedConnection(String player) {
        return null;
    }

    @Override
    public PlayerConnections myConnections() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<ConnectionKey> myOwnedConnections() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> myConnectedConnections() {
        throw new UnsupportedOperationException();
    }
}
