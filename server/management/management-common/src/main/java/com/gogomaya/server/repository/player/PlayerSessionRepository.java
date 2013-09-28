package com.gogomaya.server.repository.player;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gogomaya.player.security.PlayerSession;

@Repository
public interface PlayerSessionRepository extends JpaRepository<PlayerSession, Long> {

    public List<PlayerSession> findByPlayer(String playerId);

}
