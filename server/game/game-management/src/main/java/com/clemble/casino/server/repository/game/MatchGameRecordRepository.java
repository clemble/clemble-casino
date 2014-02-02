package com.clemble.casino.server.repository.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clemble.casino.game.MatchGameRecord;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.action.MadeMove;

@Repository
public interface MatchGameRecordRepository<State extends GameState> extends JpaRepository<MatchGameRecord<State>, GameSessionKey> {

    @Query(value = "select move from MatchGameRecord session left join session.madeMoves move where session.session = :session and move.moveId = :action")
    public MadeMove findAction(@Param("session") GameSessionKey session, @Param("action") int action);

}
