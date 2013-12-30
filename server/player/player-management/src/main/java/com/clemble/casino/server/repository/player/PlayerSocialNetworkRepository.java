package com.clemble.casino.server.repository.player;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.server.player.PlayerSocialNetwork;

@Repository
public interface PlayerSocialNetworkRepository extends GraphRepository<PlayerSocialNetwork> {

    @Query(value = "start rel=node:PlayerSocialNetwork(player={0}) match rel-[:CONNECTED]->conKey<-[:OWN]-owner return owner", elementClass = PlayerSocialNetwork.class)
    public Iterable<PlayerSocialNetwork> findRelations(String player);

}
