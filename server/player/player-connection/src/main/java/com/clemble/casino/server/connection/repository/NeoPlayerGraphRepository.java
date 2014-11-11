package com.clemble.casino.server.connection.repository;

import com.clemble.casino.server.connection.NeoPlayerGraph;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by mavarazy on 8/12/14.
 */
@Repository
public interface NeoPlayerGraphRepository extends GraphRepository<NeoPlayerGraph>, PlayerGraphRepository<NeoPlayerGraph, Long> {

    public NeoPlayerGraph findByPlayer(String player);

    @Query(value = "start p=node:playerSocialNetwork(player={0}) match p-[c:CONNECTED]->conKey<-[o:OWN]-owner return owner", elementClass = NeoPlayerGraph.class)
    public Iterable<NeoPlayerGraph> findRelations(String player);

    @Query(value = "start conKey=node:connectionKeyIndex(connectionKey={0}) match conKey<-[o:OWN]-owner return owner", elementClass = NeoPlayerGraph.class)
    public NeoPlayerGraph findOwner(String connectionKey);

}
