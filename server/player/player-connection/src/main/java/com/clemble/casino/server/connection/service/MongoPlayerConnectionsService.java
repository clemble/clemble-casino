package com.clemble.casino.server.connection.service;

import com.clemble.casino.WebMapping;
import com.clemble.casino.player.ConnectionRequest;
import com.clemble.casino.player.PlayerConnections;
import com.clemble.casino.player.event.PlayerInvitationAcceptedAction;
import com.clemble.casino.player.event.PlayerInvitationAction;
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
    public Set<String> myConnectedConnections(String me) {
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
    public ConnectionRequest connect(String player, String request) {
        ConnectionRequest newConnectionRequest = new ConnectionRequest(request);
        PlayerConnections connections = connectionsRepository.findOne(player);
        connections.getConnectionRequests().add(newConnectionRequest);
        return newConnectionRequest;
    }

    @Override
    public ConnectionRequest reply(String player, String requester, PlayerInvitationAction response) {
        ConnectionRequest connectionRequest = new ConnectionRequest(requester);
        PlayerConnections playerConnections = connectionsRepository.findOne(player);
        boolean containedRequest = playerConnections.getConnectionRequests().remove(connectionRequest);
        if (containedRequest) {
            if (response instanceof PlayerInvitationAcceptedAction) {
                playerConnections.getConnected().add(requester);
            }
        }
        return connectionRequest;
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
    public Set<String> getConnectedConnection(String player) {
        return connectionsRepository.findOne(player).getConnected();
    }

}
