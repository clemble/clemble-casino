package com.clemble.casino.server.game.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.server.game.pending.PendingGameInitiation;

@Repository
public interface PendingGameInitiationRepository extends GraphRepository<PendingGameInitiation> {

    @Query(value = "start rel=node:pendingPlayerIdx(player={0}) match rel<-[:PARTICIPATE]-initiation return initiation", elementClass = PendingGameInitiation.class)
    public List<PendingGameInitiation> findPending(String player);

}