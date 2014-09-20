package com.clemble.casino.server.game.repository;

import com.clemble.casino.game.GameRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRecordRepository extends MongoRepository<GameRecord, String> {

    public List<GameRecord> findByPlayers(String player);

}
