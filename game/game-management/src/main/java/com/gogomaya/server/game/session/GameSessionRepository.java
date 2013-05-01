package com.gogomaya.server.game.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gogomaya.server.game.action.GameSession;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {

}
