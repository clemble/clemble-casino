package com.clemble.casino.server.repository.player;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.server.player.social.PlayerSocialNetwork;

@Repository
public interface PlayerSocialNetworkRepository extends GraphRepository<PlayerSocialNetwork> {

    @Query(value = "start p=node:playerSocialNetwork(player={0}) match p-[c:CONNECTED]->conKey<-[o:OWN]-owner return owner", elementClass = PlayerSocialNetwork.class)
    public Iterable<PlayerSocialNetwork> findRelations(String player);

}
