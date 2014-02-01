package com.clemble.casino.server.repository.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.PotGameRecord;

@Repository
public interface PotGameRecordRepository extends JpaRepository<PotGameRecord, GameSessionKey> {
}
