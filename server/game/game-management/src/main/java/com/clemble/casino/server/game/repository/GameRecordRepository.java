package com.clemble.casino.server.game.repository;

import com.clemble.casino.game.GameRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clemble.casino.game.GameSessionKey;

@Repository
public interface GameRecordRepository extends JpaRepository<GameRecord, GameSessionKey> {

}
