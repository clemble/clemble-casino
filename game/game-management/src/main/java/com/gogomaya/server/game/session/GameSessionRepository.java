package com.gogomaya.server.game.session;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameState;

public interface GameSessionRepository<State extends GameState> extends JpaRepository<GameSession<State>, Long> {

}
