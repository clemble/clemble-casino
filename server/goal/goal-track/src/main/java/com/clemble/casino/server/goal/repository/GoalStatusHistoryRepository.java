package com.clemble.casino.server.goal.repository;

import com.clemble.casino.goal.GoalKey;
import com.clemble.casino.server.goal.GoalStatusHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by mavarazy on 8/16/14.
 */
public interface GoalStatusHistoryRepository extends MongoRepository<GoalStatusHistory, GoalKey> {
}
