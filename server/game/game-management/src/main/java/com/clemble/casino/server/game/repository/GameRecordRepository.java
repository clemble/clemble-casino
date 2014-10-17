package com.clemble.casino.server.game.repository;

import com.clemble.casino.game.lifecycle.record.GameRecord;
import com.clemble.casino.goal.lifecycle.record.GoalRecord;
import com.clemble.casino.lifecycle.record.RecordState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRecordRepository extends MongoRepository<GameRecord, String> {

    List<GameRecord> findByPlayers(String player);

    List<GameRecord> findByPlayersInAndState(List<String> player, RecordState state);

}
