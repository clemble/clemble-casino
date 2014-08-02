package com.clemble.casino.server.connection.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.server.connection.PlayerConnectionNetwork;

@Repository
public interface PlayerConnectionNetworkRepository extends GraphRepository<PlayerConnectionNetwork> {

    public PlayerConnectionNetwork findByPlayer(String player);

    @Query(value = "start p=node:playerSocialNetwork(player={0}) match p-[c:CONNECTED]->conKey<-[o:OWN]-owner return owner", elementClass = PlayerConnectionNetwork.class)
    public Iterable<PlayerConnectionNetwork> findRelations(String player);

}
