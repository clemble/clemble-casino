package com.clemble.casino.server.repository.game;

import com.clemble.casino.game.MatchGameRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.game.GameSessionKey;

@Repository
public interface MatchGameRecordRepository extends JpaRepository<MatchGameRecord, GameSessionKey> {
}
