package com.clemble.casino.server.connection.service;

import com.clemble.casino.player.PlayerConnections;
import com.clemble.casino.server.connection.GraphConnectionKey;
import com.clemble.casino.server.connection.GraphPlayerConnections;
import com.clemble.casino.server.connection.repository.GraphPlayerConnectionsRepository;
import com.clemble.casino.social.ClembleSocialUtils;
import com.clemble.casino.WebMapping;
import org.springframework.social.connect.ConnectionKey;

import java.util.*;

/**
 * Created by mavarazy on 8/12/14.
 */
public class GraphPlayerConnectionService extends ServerPlayerConnectionService {

    final private GraphPlayerConnectionsRepository connectionsRepository;

    public GraphPlayerConnectionService(GraphPlayerConnectionsRepository connectionsRepository){
        this.connectionsRepository = connectionsRepository;
    }

    @Override
    public PlayerConnections myConnections(String player) {
        return connectionsRepository.findByPlayer(player).toPlayerConnections();
    }

    @Override
    public Set<ConnectionKey> myOwnedConnections(String me) {
        return connectionsRepository.findByPlayer(me).toPlayerConnections().getOwned();
    }

    @Override
    public Set<String> myConnectedConnections(String me) {
        return connectionsRepository.findByPlayer(me).toPlayerConnections().getConnected();
    }

    @Override
    public PlayerConnections save(PlayerConnections connections) {
        return connectionsRepository.save(new GraphPlayerConnections(connections)).toPlayerConnections();
    }

    @Override
    public Iterable<PlayerConnections> getOwners(Collection<ConnectionKey> connections) {
        List<PlayerConnections> discoveredConnections = new ArrayList<>();
        for(ConnectionKey key: connections) {
            GraphPlayerConnections connection = connectionsRepository.findOwner(ClembleSocialUtils.toString(key));
            if (connection != null)
                discoveredConnections.add(connection.toPlayerConnections());
        }
        return discoveredConnections;
    }

    @Override
    public PlayerConnections getConnections(String player) {
        // Step 1. Generating result collection
        List<ConnectionKey> connectionKeys = new ArrayList<>();
        GraphPlayerConnections playerConnections = connectionsRepository.findByPlayer(player);;
        for(GraphConnectionKey connection: playerConnections.getConnections())
            connectionKeys.add(ClembleSocialUtils.fromString(connection.getConnectionKey()));
        // Step 2. Going through existing connections
        Iterator<GraphPlayerConnections> relatedConnections = connectionsRepository.findRelations(player).iterator();
        while (relatedConnections.hasNext()) {
            GraphPlayerConnections relatedConnection = relatedConnections.next();
            // Step 2.1. Removing owned connections
            for(GraphConnectionKey connection: relatedConnection.getOwns())
                connectionKeys.remove(ClembleSocialUtils.fromString(connection.getConnectionKey()));
            // Step 2.2. Adding internal connection
            connectionKeys.add(new ConnectionKey(WebMapping.PROVIDER_ID, relatedConnection.getPlayer()));
        }
        // Step 3. Checking values
        return connectionsRepository.findByPlayer(player).toPlayerConnections();
    }

    @Override
    public Set<ConnectionKey> getOwnedConnections(String player) {
        return connectionsRepository.findByPlayer(player).toPlayerConnections().getOwned();
    }

    @Override
    public Set<String> getConnectedConnection(String player) {
        return connectionsRepository.findByPlayer(player).toPlayerConnections().getConnected();
    }

}
