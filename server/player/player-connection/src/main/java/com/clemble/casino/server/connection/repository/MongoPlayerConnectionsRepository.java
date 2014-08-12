package com.clemble.casino.server.connection.repository;

import com.clemble.casino.server.connection.GraphConnectionKey;
import com.clemble.casino.server.connection.GraphPlayerConnections;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by mavarazy on 8/12/14.
 */
@Repository
public interface MongoPlayerConnectionsRepository extends MongoRepository<GraphPlayerConnections, String> {

    public GraphPlayerConnections findByOwns(GraphConnectionKey connectionKey);
}
