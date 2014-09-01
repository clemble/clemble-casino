package com.clemble.casino.goal.configuration.repository;

import com.clemble.casino.goal.configuration.GoalConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by mavarazy on 9/1/14.
 */
public interface GoalConfigurationRepository extends MongoRepository<GoalConfiguration, String> {
}
