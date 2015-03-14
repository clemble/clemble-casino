package com.clemble.casino.goal.repository;

import com.clemble.casino.goal.lifecycle.management.GoalVictory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by mavarazy on 3/14/15.
 */
public interface GoalVictoryRepository extends MongoRepository<GoalVictory, String> {

    List<GoalVictory> findByPlayer(String player);

}
