package com.clemble.casino.goal.construction.repository;

import com.clemble.casino.lifecycle.initiation.InitiationState;
import com.clemble.casino.goal.lifecycle.initiation.GoalInitiation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by mavarazy on 9/13/14.
 */
public interface GoalInitiationRepository extends MongoRepository<GoalInitiation, String>{

    public List<GoalInitiation> findByPlayer(String player);

    public List<GoalInitiation> findByPlayerAndState(String player, InitiationState state);

    public List<GoalInitiation> findByState(InitiationState state);

}
