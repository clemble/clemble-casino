package com.clemble.casino.server.goal.repository;

import com.clemble.casino.goal.PlayerGoal;
import com.clemble.casino.goal.PlayerGoalState;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by mavarazy on 8/2/14.
 */
public interface PlayerGoalRepository extends MongoRepository<PlayerGoal, String> {

    public List<PlayerGoal> findByPlayer(String player);

    public List<PlayerGoal> findByPlayerAndState(String player, PlayerGoalState state);

}
