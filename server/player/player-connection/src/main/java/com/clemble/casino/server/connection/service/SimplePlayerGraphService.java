package com.clemble.casino.server.connection.service;

import com.clemble.casino.server.connection.PlayerGraph;
import com.clemble.casino.server.connection.repository.PlayerGraphRepository;
import com.google.common.collect.ImmutableList;
import org.springframework.social.connect.ConnectionKey;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by mavarazy on 8/12/14.
 */
public class SimplePlayerGraphService<T extends PlayerGraph, ID extends Serializable> implements PlayerGraphService {

    final private PlayerGraphRepository<T, ID> graphRepository;
    final private Function<String, T> graphFactory;

    public SimplePlayerGraphService(Function<String, T> graphFactory, PlayerGraphRepository graphRepository){
        this.graphRepository = graphRepository;
        this.graphFactory = graphFactory;
    }

    @Override
    public Set<String> myConnections() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getConnections(String me) {
        return graphRepository.findByPlayer(me).fetchConnections();
    }

    @Override
    public boolean addOwned(String player, ConnectionKey connectionKey) {
        // Step 1. Fetching related connections
        T connections = graphRepository.findByPlayer(player);
        // Step 2. Adding owned to connectionKeys
        connections = connections == null ? graphFactory.apply(player) : connections;
        connections.addOwned(connectionKey);
        return graphRepository.save(connections) != null;
    }

    @Override
    public boolean create(String player) {
        return graphRepository.save(graphFactory.apply(player)) != null;
    }

    @Override
    public Iterable<String> getOwners(Collection<ConnectionKey> connections) {
        return graphRepository.
            findByOwnedIn(connections).
            stream().
            map(g -> g.getPlayer()).
            collect(Collectors.toSet());
    }

    @Override
    public boolean connect(String a, String b) {
        // Step 1. Updating graph A
        T aGraph = graphRepository.findByPlayer(a);
        aGraph.addConnection(b);
        // Step 2. Updating graph B
        T bGraph = graphRepository.findByPlayer(b);
        bGraph.addConnection(a);
        return graphRepository.save(ImmutableList.of(aGraph, bGraph)) != null;
    }

}
