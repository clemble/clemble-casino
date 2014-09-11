package com.clemble.casino.goal.construction.spring;

import com.clemble.casino.goal.construction.GoalKeyGenerator;
import com.clemble.casino.goal.construction.controller.GoalConstructionServiceController;
import com.clemble.casino.goal.construction.repository.GoalConstructionRepository;
import com.clemble.casino.goal.construction.service.SelfGoalConstructionService;
import com.clemble.casino.goal.construction.service.ServerGoalConstructionService;
import com.clemble.casino.server.key.RedisKeyFactory;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.RedisSpringConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import redis.clients.jedis.JedisPool;

/**
 * Created by mavarazy on 9/10/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class, MongoSpringConfiguration.class, RedisSpringConfiguration.class})
public class GoalConstructionSpringConfiguration {

    @Bean
    public GoalConstructionRepository goalConstructionRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(GoalConstructionRepository.class);
    }

    @Bean
    public ServerGoalConstructionService goalConstructionService(SelfGoalConstructionService constructionService, GoalConstructionRepository repository) {
        return new ServerGoalConstructionService(constructionService, repository);
    }

    @Bean
    public GoalConstructionServiceController goalConstructionServiceController(ServerGoalConstructionService constructionService) {
        return new GoalConstructionServiceController(constructionService);
    }

    @Bean
    public GoalKeyGenerator goalKeyGenerator(JedisPool jedisPool) {
        return new GoalKeyGenerator(new RedisKeyFactory("GOAL_COUNTER", "A", jedisPool));
    }

    @Bean
    public SelfGoalConstructionService selfGoalConstructionService(GoalKeyGenerator keyGenerator, GoalConstructionRepository constructionRepository) {
        return new SelfGoalConstructionService(keyGenerator, constructionRepository);
    }


}
