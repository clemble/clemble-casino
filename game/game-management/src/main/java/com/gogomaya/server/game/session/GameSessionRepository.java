package com.gogomaya.server.game.session;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogomaya.server.game.action.GameSession;

public interface GameSessionRepository<T extends GameSession<?>> extends JpaRepository<T, Long> {

}
