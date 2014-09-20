package com.clemble.casino.goal.repository;

import com.clemble.casino.goal.management.GoalRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by mavarazy on 9/20/14.
 */
public interface GoalRecordRepository extends MongoRepository<GoalRecord, String> {

    public List<GoalRecord> findByPlayer(String player);

}
