package com.clemble.casino.goal.repository;

import com.clemble.casino.goal.lifecycle.management.GoalState;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

/**
 * Created by mavarazy on 10/9/14.
 */
public interface GoalStateRepository extends MongoRepository<GoalState, String> {

    List<GoalState> findByPlayer(String player);

    List<GoalState> findByPlayerIn(Set<String> players);

}
