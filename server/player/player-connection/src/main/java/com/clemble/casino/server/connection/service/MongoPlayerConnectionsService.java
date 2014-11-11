package com.clemble.casino.server.connection.service;

import com.clemble.casino.player.FriendInvitation;
import com.clemble.casino.player.PlayerConnections;
import com.clemble.casino.player.event.PlayerInvitationAcceptedAction;
import com.clemble.casino.player.event.PlayerInvitationAction;
import com.clemble.casino.server.connection.repository.MongoPlayerConnectionsRepository;
import org.springframework.social.connect.ConnectionKey;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by mavarazy on 8/12/14.
 */
public class MongoPlayerConnectionsService implements ServerPlayerConnectionService {

    final private MongoPlayerConnectionsRepository connectionsRepository;

    public MongoPlayerConnectionsService(MongoPlayerConnectionsRepository connectionsRepository) {
        this.connectionsRepository = connectionsRepository;
    }

    @Override
    public Set<String> myConnections() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getConnections(String me) {
        return connectionsRepository.findOne(me).getConnected();
    }

    @Override
    public PlayerConnections save(PlayerConnections connections) {
        return connectionsRepository.save(connections);
    }

    @Override
    public PlayerConnections getServerConnection(String player) {
        return connectionsRepository.findOne(player);
    }

    @Override
    public List<PlayerConnections> getOwners(Collection<ConnectionKey> connectionKeys) {
        return connectionsRepository.findByOwnedIn(connectionKeys);
    }

}
