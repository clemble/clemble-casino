package com.clemble.casino.goal.construction.repository;

import com.clemble.casino.goal.construction.GoalInitiation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.cdi.MongoRepositoryExtension;

/**
 * Created by mavarazy on 9/13/14.
 */
public interface GoalInitiationRepository extends MongoRepository<GoalInitiation, String>{
}
