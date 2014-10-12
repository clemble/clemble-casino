package com.clemble.casino.goal.repository;

import com.clemble.casino.goal.lifecycle.management.GoalState;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by mavarazy on 10/9/14.
 */
public interface GoalStateRepository extends MongoRepository<GoalState, String>{

    public List<GoalState> findByPlayer(String player);

}
