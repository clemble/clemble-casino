package com.gogomaya.server.repository.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gogomaya.game.GameSession;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.GameState;
import com.gogomaya.game.event.client.MadeMove;

@Repository
public interface GameSessionRepository<State extends GameState> extends JpaRepository<GameSession<State>, GameSessionKey> {

    @Query(value = "select move from GameSession session left join session.madeMoves move where session.session = :session and move.moveId = :action")
    public MadeMove findAction(@Param("session") GameSessionKey session, @Param("action") int action);

}
