package com.clemble.casino.server.repository.player;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.player.security.PlayerSession;

@Repository
public interface PlayerSessionRepository extends MongoRepository<PlayerSession, String> {

    public List<PlayerSession> findByPlayer(String playerId);

}
