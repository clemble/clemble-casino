package com.clemble.casino.server.connection.service;

import com.clemble.casino.player.FriendInvitation;
import com.clemble.casino.player.PlayerConnections;
import com.clemble.casino.player.event.PlayerInvitationAcceptedAction;
import com.clemble.casino.player.event.PlayerInvitationAction;
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
public class GraphPlayerConnectionService implements ServerPlayerConnectionService {

    final private GraphPlayerConnectionsRepository connectionsRepository;

    public GraphPlayerConnectionService(GraphPlayerConnectionsRepository connectionsRepository){
        this.connectionsRepository = connectionsRepository;
    }

    @Override
    public Set<String> myConnections() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getConnections(String me) {
        return connectionsRepository.findByPlayer(me).toPlayerConnections().getConnected();
    }

    @Override
    public PlayerConnections save(PlayerConnections connections) {
        return connectionsRepository.save(new GraphPlayerConnections(connections)).toPlayerConnections();
    }

    @Override
    public PlayerConnections getServerConnection(String player) {
        return connectionsRepository.findByPlayer(player).toPlayerConnections();
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

}
