package com.clemble.casino.goal.repository;

import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.ShortGoalState;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by mavarazy on 10/9/14.
 */
public interface ShortGoalStateRepository extends MongoRepository<ShortGoalState, String> {

    List<ShortGoalState> findByPlayer(String player);

    List<ShortGoalState> findByPlayerIn(Set<String> players);

}
