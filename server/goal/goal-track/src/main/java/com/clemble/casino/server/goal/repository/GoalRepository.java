package com.clemble.casino.server.goal.repository;

import com.clemble.casino.goal.Goal;
import com.clemble.casino.goal.GoalState;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by mavarazy on 8/2/14.
 */
public interface GoalRepository extends MongoRepository<Goal, String> {

    public List<Goal> findByPlayer(String player);

    public List<Goal> findByPlayerAndState(String player, GoalState state);

}
