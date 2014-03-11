package com.clemble.casino.server.repository.game;

import com.clemble.casino.game.RoundGameRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.action.MadeMove;

@Repository
public interface RoundGameRecordRepository extends JpaRepository<RoundGameRecord, GameSessionKey> {

}
