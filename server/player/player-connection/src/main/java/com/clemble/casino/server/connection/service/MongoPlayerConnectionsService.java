package com.clemble.casino.server.connection.service;

import com.clemble.casino.player.PlayerConnections;
import com.clemble.casino.server.connection.GraphConnectionKey;
import com.clemble.casino.server.connection.GraphPlayerConnections;
import com.clemble.casino.server.connection.repository.MongoPlayerConnectionsRepository;
import org.springframework.social.connect.ConnectionKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by mavarazy on 8/12/14.
 */
public class MongoPlayerConnectionsService extends ServerPlayerConnectionService {

    final private MongoPlayerConnectionsRepository connectionsRepository;

    public MongoPlayerConnectionsService(MongoPlayerConnectionsRepository connectionsRepository) {
        this.connectionsRepository = connectionsRepository;
    }

    @Override
    public PlayerConnections myConnections(String me) {
        return connectionsRepository.findOne(me);
    }

    @Override
    public Set<ConnectionKey> myOwnedConnections(String me) {
        return connectionsRepository.findOne(me).getOwned();
    }

    @Override
    public Set<ConnectionKey> myConnectedConnections(String me) {
        return connectionsRepository.findOne(me).getConnected();
    }

    @Override
    public PlayerConnections save(PlayerConnections connections) {
        return connectionsRepository.save(connections);
    }

    @Override
    public Iterable<PlayerConnections> getOwners(Collection<ConnectionKey> connections) {
        return connectionsRepository.findByOwnedIn(connections);
    }

    @Override
    public PlayerConnections getConnections(String player) {
        return connectionsRepository.findOne(player);
    }

    @Override
    public Set<ConnectionKey> getOwnedConnections(String player) {
        return connectionsRepository.findOne(player).getOwned();
    }

    @Override
    public Set<ConnectionKey> getConnectedConnection(String player) {
        return connectionsRepository.findOne(player).getConnected();
    }

}
