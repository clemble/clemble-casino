package com.gogomaya.server.repository.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;

@Repository
public interface GameSessionRepository<State extends GameState> extends JpaRepository<GameSession<State>, Long> {

}
