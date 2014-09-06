package com.clemble.casino.server.goal.spring;

import com.clemble.casino.bet.BetSpecification;
import com.clemble.casino.server.goal.GoalKeyGenerator;
import com.clemble.casino.server.goal.repository.GoalRepository;
import com.clemble.casino.server.goal.controller.GoalServiceController;
import com.clemble.casino.server.goal.service.BidCalculator;
import com.clemble.casino.server.goal.service.SpecificationBidCalculator;
import com.clemble.casino.server.key.RedisKeyFactory;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.RedisSpringConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import redis.clients.jedis.JedisPool;

/**
 * Created by mavarazy on 8/2/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class, RedisSpringConfiguration.class, MongoSpringConfiguration.class})
public class GoalSpringConfiguration {

    @Bean
    public GoalKeyGenerator goalKeyGenerator(JedisPool jedisPool) {
        return new GoalKeyGenerator(new RedisKeyFactory("GOAL_COUNTER", "A", jedisPool));
    }

    @Bean
    public GoalRepository playerGoalRepository(MongoRepositoryFactory mongoRepositoryFactory) {
        return mongoRepositoryFactory.getRepository(GoalRepository.class);
    }

    @Bean
    public BidCalculator bidCalculator() {
        return new SpecificationBidCalculator(new BetSpecification(Long.MAX_VALUE, 10, 20));
    }

    @Bean
    public GoalServiceController playerGoalController(@Qualifier("goalKeyGenerator") GoalKeyGenerator goalKeyGenerator, BidCalculator bidCalculator, GoalRepository goalRepository, SystemNotificationService notificationService) {
        return new GoalServiceController(goalKeyGenerator, bidCalculator, goalRepository, notificationService);
    }

}
