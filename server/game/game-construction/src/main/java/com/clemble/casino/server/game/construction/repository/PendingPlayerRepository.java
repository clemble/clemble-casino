package com.clemble.casino.server.game.construction.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.server.game.construction.PendingGameInitiation;
import com.clemble.casino.server.game.construction.PendingPlayer;

@Repository
public interface PendingPlayerRepository  extends GraphRepository<PendingPlayer>{

    @Query(value = "start player=node:pendingPlayerIdx(player={0}) match player<-[:PARTICIPATE]-initiation return initiation", elementClass = PendingGameInitiation.class)
    public List<PendingGameInitiation> findPending(String player);

}
