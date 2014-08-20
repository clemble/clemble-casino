package com.clemble.casino.server.game.repository;

import com.clemble.casino.game.GameRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRecordRepository extends MongoRepository<GameRecord, String> {

}
