package com.clemble.casino.server.goal.spring;

import com.clemble.casino.server.goal.repository.GoalRepository;
import com.clemble.casino.server.goal.controller.GoalServiceController;
import com.clemble.casino.server.id.IdGenerator;
import com.clemble.casino.server.id.RedisIdGenerator;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.RedisSpringConfiguration;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import redis.clients.jedis.JedisPool;

import java.net.UnknownHostException;

/**
 * Created by mavarazy on 8/2/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class, RedisSpringConfiguration.class})
public class GoalSpringConfiguration {

    @Bean
    public IdGenerator goalIdGenerator(JedisPool jedisPool) {
        return new RedisIdGenerator("GOAL_COUNTER", "P", jedisPool);
    }

    @Bean
    public MongoRepositoryFactory playerGoalRepositoryFactory(@Value("${clemble.db.mongo.host}") String host, @Value("${clemble.db.mongo.port}") int port) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(host, port);
        MongoOperations mongoOperations = new MongoTemplate(mongoClient, "clemble");
        return new MongoRepositoryFactory(mongoOperations);
    }

    @Bean
    public GoalRepository playerGoalRepository(@Qualifier("playerGoalRepositoryFactory") MongoRepositoryFactory playerGoalRepositoryFactory) {
        return playerGoalRepositoryFactory.getRepository(GoalRepository.class);
    }

    @Bean
    public GoalServiceController playerGoalController(@Qualifier("goalIdGenerator") IdGenerator goalIdGenerator,GoalRepository goalRepository) {
        return new GoalServiceController(goalIdGenerator, goalRepository);
    }

}
