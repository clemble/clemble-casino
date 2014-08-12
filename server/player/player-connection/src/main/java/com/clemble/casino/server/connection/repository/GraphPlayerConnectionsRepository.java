package com.clemble.casino.server.connection.repository;

import com.clemble.casino.server.connection.GraphPlayerConnections;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by mavarazy on 8/12/14.
 */
@Repository
public interface GraphPlayerConnectionsRepository extends GraphRepository<GraphPlayerConnections> {

    public GraphPlayerConnections findByPlayer(String player);

    @Query(value = "start p=node:playerSocialNetwork(player={0}) match p-[c:CONNECTED]->conKey<-[o:OWN]-owner return owner", elementClass = GraphPlayerConnections.class)
    public Iterable<GraphPlayerConnections> findRelations(String player);

    @Query(value = "start conKey=node:connectionKeyIndex(connectionKey={0}) match conKey<-[o:OWN]-owner return owner", elementClass = GraphPlayerConnections.class)
    public GraphPlayerConnections findOwner(String connectionKey);

}
