package com.clemble.casino.server.connection.service;

import com.clemble.casino.server.connection.GraphConnectionKey;
import com.clemble.casino.server.connection.GraphPlayerConnections;
import com.clemble.casino.server.connection.repository.MongoPlayerConnectionsRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mavarazy on 8/12/14.
 */
public class MongoPlayerConnectionsService {

    final private MongoPlayerConnectionsRepository repository;

    public MongoPlayerConnectionsService(MongoPlayerConnectionsRepository repository) {
        this.repository = repository;
    }


    public Iterable<GraphPlayerConnections> findRelations(String player) {
        List<GraphPlayerConnections> relations = new ArrayList<>();
        for(GraphConnectionKey connectionKey: repository.findOne(player).getConnections()) {
            GraphPlayerConnections relation = repository.findByOwns(connectionKey);
            if (relation != null) {
                relations.add(relation);
            }
        }
        return relations;
    }

    public GraphPlayerConnections save(GraphPlayerConnections connection) {
        return repository.save(connection);
    }
}
