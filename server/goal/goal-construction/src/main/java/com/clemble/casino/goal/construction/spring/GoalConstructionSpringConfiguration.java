package com.clemble.casino.goal.construction.spring;

import com.clemble.casino.lifecycle.initiation.InitiationState;
import com.clemble.casino.goal.construction.GoalInitiation;
import com.clemble.casino.goal.construction.GoalInitiationExpirationTask;
import com.clemble.casino.goal.construction.GoalInitiationTaskAdapter;
import com.clemble.casino.goal.construction.GoalKeyGenerator;
import com.clemble.casino.goal.construction.controller.GoalConstructionServiceController;
import com.clemble.casino.goal.construction.controller.GoalInitiationServiceController;
import com.clemble.casino.goal.construction.listener.SystemGoalInitiationExpirationEventListener;
import com.clemble.casino.goal.construction.repository.GoalConstructionRepository;
import com.clemble.casino.goal.construction.repository.GoalInitiationRepository;
import com.clemble.casino.goal.construction.service.SelfGoalConstructionService;
import com.clemble.casino.goal.construction.service.ServerGoalConstructionService;
import com.clemble.casino.goal.construction.service.ServerGoalInitiationService;
import com.clemble.casino.payment.service.PlayerAccountServiceContract;
import com.clemble.casino.server.executor.EventTaskAdapter;
import com.clemble.casino.server.executor.EventTaskExecutor;
import com.clemble.casino.server.key.RedisKeyFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.PaymentClientSpringConfiguration;
import com.clemble.casino.server.spring.common.RedisSpringConfiguration;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
        @Qualifier("playerNotificationService") PlayerNotificationService notificationService,
        @Qualifier("goalInitiationEventTaskExecutor") EventTaskExecutor taskExecutor){
        return new ServerGoalInitiationService(initiationRepository, notificationService, taskExecutor);
    }

    @Bean
    public SelfGoalConstructionService selfGoalConstructionService(
        GoalKeyGenerator keyGenerator,
        GoalConstructionRepository constructionRepository,
        ServerGoalInitiationService initiationService,
        @Qualifier("playerAccountClient") PlayerAccountServiceContract accountServiceContract) {
        return new SelfGoalConstructionService(keyGenerator, initiationService, constructionRepository, accountServiceContract);
    }

    @Bean
    public EventTaskAdapter goalInitiationTaskAdapter(SystemNotificationService notificationService){
        return new GoalInitiationTaskAdapter(notificationService);
    }

    @Bean
    public EventTaskExecutor goalInitiationEventTaskExecutor(@Qualifier("goalInitiationTaskAdapter") EventTaskAdapter gameEventTaskAdapter, GoalInitiationRepository initiationRepository) {
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder().setNameFormat("CL goal:initiation:event:executor - %d");
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5, threadFactoryBuilder.build());
        EventTaskExecutor taskExecutor = new EventTaskExecutor(gameEventTaskAdapter, executorService);
        // Step 1. Restoring unfinished initiations
        List<GoalInitiation> pendingInitiations = initiationRepository.findByState(InitiationState.pending);
        pendingInitiations.forEach(initiation -> taskExecutor.schedule(new GoalInitiationExpirationTask(initiation.getGoalKey(), initiation.getStartDate())));
        // Step 2. Returning task executor
        return taskExecutor;
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
