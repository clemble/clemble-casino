package com.clemble.casino.goal.construction.spring;

import com.clemble.casino.goal.construction.GoalKeyGenerator;
import com.clemble.casino.goal.construction.controller.GoalConstructionServiceController;
import com.clemble.casino.goal.construction.controller.GoalInitiationServiceController;
import com.clemble.casino.goal.construction.listener.SystemGoalInitiationDueEventListener;
import com.clemble.casino.goal.construction.listener.SystemGoalInitiationStartedEventListener;
import com.clemble.casino.goal.construction.repository.GoalConstructionRepository;
import com.clemble.casino.goal.construction.repository.GoalInitiationRepository;
import com.clemble.casino.goal.construction.service.SelfGoalConstructionService;
import com.clemble.casino.goal.construction.service.ServerGoalConstructionService;
import com.clemble.casino.goal.construction.service.ServerGoalInitiationService;
import com.clemble.casino.payment.service.PlayerAccountService;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.key.RedisKeyFactory;
import com.clemble.casino.server.player.notification.ServerNotificationService;
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
    public GoalKeyGenerator goalKeyGenerator(JedisPool jedisPool) {
        return new GoalKeyGenerator(new RedisKeyFactory("GOAL_COUNTER", "A", jedisPool));
    }

    @Bean
    public ServerGoalInitiationService serverGoalInitiationService(
        GoalInitiationRepository initiationRepository,
        @Qualifier("playerNotificationService") ServerNotificationService notificationService,
        SystemNotificationService systemNotificationService){
        return new ServerGoalInitiationService(initiationRepository, notificationService, systemNotificationService);
    }

    @Bean
    public SelfGoalConstructionService selfGoalConstructionService(
        GoalKeyGenerator keyGenerator,
        GoalConstructionRepository constructionRepository,
        SystemNotificationService notificationService,
        @Qualifier("playerAccountClient") PlayerAccountService accountServiceContract) {
        return new SelfGoalConstructionService(keyGenerator, notificationService, constructionRepository, accountServiceContract);
    }

    @Bean
    public SystemGoalInitiationDueEventListener systemGoalInitiationExpirationEventListener(
        SystemNotificationServiceListener notificationServiceListener,
        SystemNotificationService notificationService,
        GoalInitiationRepository initiationRepository) {
        SystemGoalInitiationDueEventListener eventListener = new SystemGoalInitiationDueEventListener(notificationService, initiationRepository);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Bean
    public SystemGoalInitiationStartedEventListener systemGoalInitiationStartedEventListener(
        SystemNotificationServiceListener notificationServiceListener,
        ServerGoalInitiationService initiationService
    ) {
        // Step 1. Generating event listener
        SystemGoalInitiationStartedEventListener eventListener = new SystemGoalInitiationStartedEventListener(initiationService);
        // Step 2. Subscribing to event listener
        notificationServiceListener.subscribe(eventListener);
        // Step 3. Returning event listener
        return eventListener;
    }

}
