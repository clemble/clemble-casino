package com.clemble.casino.server.connection.service;

import com.clemble.casino.player.ConnectionRequest;
import com.clemble.casino.player.PlayerConnections;
import com.clemble.casino.player.event.PlayerInvitationAction;
import com.clemble.casino.player.service.PlayerConnectionService;
import org.springframework.social.connect.ConnectionKey;

import java.util.Collection;
import java.util.Set;

/**
 * Created by mavarazy on 8/12/14.
 */
abstract public class ServerPlayerConnectionService implements PlayerConnectionService {

    @Override
    final public PlayerConnections myConnections() {
        throw new IllegalAccessError();
    }

    abstract public PlayerConnections myConnections(String me);

    @Override
    final public Set<ConnectionKey> myOwnedConnections() {
        throw new IllegalAccessError();
    }

    abstract public Set<ConnectionKey> myOwnedConnections(String me);

    @Override
    final public Set<String> myConnectedConnections() {
        throw new IllegalAccessError();
    }

    abstract public Set<String> myConnectedConnections(String me);

    abstract public PlayerConnections save(PlayerConnections connections);

    abstract public Iterable<PlayerConnections> getOwners(Collection<ConnectionKey> connections);

    public ConnectionRequest connect(String player) {
        throw new IllegalAccessError();
    }

    abstract public ConnectionRequest connect(String player, String request);

    public ConnectionRequest reply(String player, PlayerInvitationAction response) {
        throw new IllegalAccessError();
    }

    abstract public ConnectionRequest reply(String player, String requester, PlayerInvitationAction response);

}
