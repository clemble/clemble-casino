package com.clemble.casino.goal.construction.spring;

import com.clemble.casino.goal.construction.GoalInitiationTaskAdapter;
import com.clemble.casino.goal.construction.GoalKeyGenerator;
import com.clemble.casino.goal.construction.controller.GoalConstructionServiceController;
import com.clemble.casino.goal.construction.repository.GoalConstructionRepository;
import com.clemble.casino.goal.construction.repository.GoalInitiationRepository;
import com.clemble.casino.goal.construction.service.SelfGoalConstructionService;
import com.clemble.casino.goal.construction.service.ServerGoalConstructionService;
import com.clemble.casino.goal.construction.service.ServerGoalInitiationService;
import com.clemble.casino.server.executor.EventTaskAdapter;
import com.clemble.casino.server.executor.EventTaskExecutor;
import com.clemble.casino.server.key.RedisKeyFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.RedisSpringConfiguration;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
        ServerGoalInitiationService initiationService) {
        return new SelfGoalConstructionService(keyGenerator, initiationService, constructionRepository);
    }

    @Bean
    public EventTaskAdapter goalInitiationTaskAdapter(){
        return new GoalInitiationTaskAdapter();
    }

    @Bean
    public EventTaskExecutor goalInitiationEventTaskExecutor(@Qualifier("goalInitiationTaskAdapter") EventTaskAdapter gameEventTaskAdapter) {
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder().setNameFormat("CL goal:initiation:event:executor - %d");
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5, threadFactoryBuilder.build());
        return new EventTaskExecutor(gameEventTaskAdapter, executorService);
    }

}
