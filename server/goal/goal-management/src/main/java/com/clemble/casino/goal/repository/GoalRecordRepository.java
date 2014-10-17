package com.clemble.casino.goal.repository;

import com.clemble.casino.goal.lifecycle.record.GoalRecord;
import com.clemble.casino.lifecycle.record.RecordState;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by mavarazy on 9/20/14.
 */
public interface GoalRecordRepository extends MongoRepository<GoalRecord, String> {

    public List<GoalRecord> findByPlayer(String player);

    public List<GoalRecord> findByPlayerAndState(String player, RecordState state);

}
