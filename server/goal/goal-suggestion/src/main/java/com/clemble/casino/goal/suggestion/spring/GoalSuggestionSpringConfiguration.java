package com.clemble.casino.goal.suggestion.spring;

import com.clemble.casino.goal.suggestion.*;
import com.clemble.casino.goal.suggestion.controller.GoalSuggestionServiceController;
import com.clemble.casino.goal.suggestion.repository.GoalSuggestionRepository;
import com.clemble.casino.server.key.RedisKeyFactory;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.spring.common.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import redis.clients.jedis.JedisPool;

/**
 * Created by mavarazy on 1/3/15.
 */
@Configuration
@Import({
    CommonSpringConfiguration.class,
    PaymentClientSpringConfiguration.class,
    MongoSpringConfiguration.class,
    RedisSpringConfiguration.class,
    RabbitSpringConfiguration.class})
public class GoalSuggestionSpringConfiguration {

    @Bean
    public GoalSuggestionRepository goalSuggestionRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(GoalSuggestionRepository.class);
    }

    @Bean
    public GoalSuggestionKeyGenerator goalSuggestionKeyGenerator(JedisPool jedisPool){
        return new GoalSuggestionKeyGenerator(new RedisKeyFactory("GOAL_SUGGESTION_COUNTER", "B", jedisPool));
    }

    @Bean
    public GoalSuggestionServiceController goalSuggestionServiceController(
        GoalSuggestionKeyGenerator goalKeyGenerator,
        SystemNotificationService notificationService,
        @Qualifier("playerNotificationService") ServerNotificationService playerNotificationService,
        GoalSuggestionRepository suggestionRepository){
        return new GoalSuggestionServiceController(
            goalKeyGenerator,
            playerNotificationService,
            notificationService,
            suggestionRepository);
    }

}
