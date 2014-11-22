package com.clemble.casino.goal.construction.spring;

import com.clemble.casino.goal.construction.GoalKeyGenerator;
import com.clemble.casino.goal.construction.controller.FriendInitiationServiceController;
import com.clemble.casino.goal.construction.controller.GoalConstructionServiceController;
import com.clemble.casino.goal.construction.controller.GoalInitiationServiceController;
import com.clemble.casino.goal.construction.listener.SystemGoalInitiationExpirationEventListener;
import com.clemble.casino.goal.construction.repository.GoalConstructionRepository;
import com.clemble.casino.goal.construction.repository.GoalInitiationRepository;
import com.clemble.casino.goal.lifecycle.construction.service.SelfGoalConstructionService;
import com.clemble.casino.goal.lifecycle.construction.service.ServerGoalConstructionService;
import com.clemble.casino.goal.lifecycle.construction.service.ServerGoalInitiationService;
import com.clemble.casino.payment.service.PlayerAccountService;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.key.RedisKeyFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import redis.clients.jedis.JedisPool;

/**
 * Created by mavarazy on 9/10/14.
 */
@Configuration
@Import({
    CommonSpringConfiguration.class,
    PaymentClientSpringConfiguration.class,
    MongoSpringConfiguration.class,
    RedisSpringConfiguration.class})
public class GoalConstructionSpringConfiguration {

    @Bean
    public GoalConstructionRepository goalConstructionRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(GoalConstructionRepository.class);
    }

    @Bean
    public GoalInitiationRepository goalInitiationRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(GoalInitiationRepository.class);
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
    public GoalInitiationServiceController goalInitiationServiceController(ServerGoalInitiationService initiationService) {
        return new GoalInitiationServiceController(initiationService);
    }

    @Bean
    public FriendInitiationServiceController friendInitiationServiceController(
        GoalInitiationRepository initiationRepository,
        @Qualifier("playerConnectionClient") PlayerConnectionService connectionService) {
        return new FriendInitiationServiceController(initiationRepository, connectionService);
    }

    @Bean
    public GoalKeyGenerator goalKeyGenerator(JedisPool jedisPool) {
        return new GoalKeyGenerator(new RedisKeyFactory("GOAL_COUNTER", "A", jedisPool));
    }

    @Bean
    public ServerGoalInitiationService serverGoalInitiationService(
        GoalInitiationRepository initiationRepository,
        @Qualifier("playerNotificationService") PlayerNotificationService notificationService,
        SystemNotificationService systemNotificationService){
        return new ServerGoalInitiationService(initiationRepository, notificationService, systemNotificationService);
    }

    @Bean
    public SelfGoalConstructionService selfGoalConstructionService(
        GoalKeyGenerator keyGenerator,
        GoalConstructionRepository constructionRepository,
        ServerGoalInitiationService initiationService,
        @Qualifier("playerAccountClient") PlayerAccountService accountServiceContract) {
        return new SelfGoalConstructionService(keyGenerator, initiationService, constructionRepository, accountServiceContract);
    }

    @Bean
    public SystemGoalInitiationExpirationEventListener systemGoalInitiationExpirationEventListener(
        SystemNotificationServiceListener notificationServiceListener,
        SystemNotificationService notificationService,
        GoalInitiationRepository initiationRepository) {
        SystemGoalInitiationExpirationEventListener eventListener = new SystemGoalInitiationExpirationEventListener(notificationService, initiationRepository);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

}
