package com.clemble.casino.goal.spring;

import com.clemble.casino.goal.action.GoalManagerFactoryFacade;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.aspect.GoalAspectFactory;
import com.clemble.casino.goal.aspect.outcome.GoalMissedOutcomeAspectFactory;
import com.clemble.casino.goal.aspect.outcome.GoalReachedOutcomeAspectFactory;
import com.clemble.casino.goal.aspect.persistence.GoalPersistenceAspectFactory;
import com.clemble.casino.goal.aspect.record.GoalRecordAspectFactory;
import com.clemble.casino.goal.aspect.time.GoalTimeAspectFactory;
import com.clemble.casino.goal.controller.GoalActionServiceController;
import com.clemble.casino.goal.controller.GoalRecordServiceController;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lisetener.GoalEventTaskAdapter;
import com.clemble.casino.goal.lisetener.SystemGoalStartedEventListener;
import com.clemble.casino.goal.repository.GoalRecordRepository;
import com.clemble.casino.goal.repository.GoalStateRepository;
import com.clemble.casino.server.action.ClembleManagerFactory;
import com.clemble.casino.server.executor.EventTaskAdapter;
import com.clemble.casino.server.executor.EventTaskExecutor;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by mavarazy on 9/12/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class, MongoSpringConfiguration.class})
public class GoalManagementSpringConfiguration implements SpringConfiguration {

    @Bean
    public GoalRecordRepository goalRecordRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(GoalRecordRepository.class);
    }

    @Bean
    public GoalStateRepository goalStateRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(GoalStateRepository.class);
    }

    @Bean
    public GoalRecordServiceController goalRecordServiceController(GoalRecordRepository recordRepository) {
        return new GoalRecordServiceController(recordRepository);
    }

    @Bean
    public GoalActionServiceController goalActionServiceController(GoalManagerFactoryFacade factoryFacade, GoalStateRepository goalStateRepository) {
        return new GoalActionServiceController(factoryFacade, goalStateRepository);
    }

    @Bean
    public ClembleManagerFactory<GoalConfiguration> goalManagerFactory() {
        return new ClembleManagerFactory<>(GoalAspectFactory.class);
    }

    @Bean
    public GoalManagerFactoryFacade goalManagerFactoryFacade(
        ClembleManagerFactory<GoalConfiguration> goalManagerFactory,
        GoalRecordRepository recordRepository,
        GoalStateRepository goalStateRepository,
        @Qualifier("playerNotificationService") PlayerNotificationService notificationService) {
        return new GoalManagerFactoryFacade(goalManagerFactory, recordRepository, goalStateRepository, notificationService);
    }

    @Bean
    public SystemGoalStartedEventListener systemGoalStartedEventListener(
        SystemNotificationServiceListener notificationServiceListener,
        GoalManagerFactoryFacade goalManagerFactoryFacade) {
        SystemGoalStartedEventListener eventListener = new SystemGoalStartedEventListener(goalManagerFactoryFacade);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Bean
    public GoalRecordAspectFactory goalRecordAspectFactory(GoalRecordRepository recordRepository){
        return new GoalRecordAspectFactory(recordRepository);
    }

    @Bean
    public GoalTimeAspectFactory goalTimeAspectFactory(@Qualifier("goalManagementEventTaskExecutor") EventTaskExecutor taskExecutor){
        return new GoalTimeAspectFactory(taskExecutor);
    }

    @Bean
    public GoalPersistenceAspectFactory goalPersistenceAspectFactory(GoalStateRepository stateRepository) {
        return new GoalPersistenceAspectFactory(stateRepository);
    }

    @Bean
    public EventTaskAdapter goalManagementEventTaskAdapter(GoalManagerFactoryFacade managerFactoryFacade){
        return new GoalEventTaskAdapter(managerFactoryFacade);
    }

    @Bean
    public EventTaskExecutor goalManagementEventTaskExecutor(@Qualifier("goalManagementEventTaskAdapter") EventTaskAdapter goalManagementEventTaskAdapter) {
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder().setNameFormat("CL goal:management:event:executor - %d");
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5, threadFactoryBuilder.build());
        return new EventTaskExecutor(goalManagementEventTaskAdapter, executorService);
    }

    @Bean
    public GoalMissedOutcomeAspectFactory goalMissedOutcomeAspectFactory(SystemNotificationService systemNotificationService) {
        return new GoalMissedOutcomeAspectFactory(systemNotificationService);
    }

    @Bean
    public GoalReachedOutcomeAspectFactory goalReachedOutcomeAspectFactory(SystemNotificationService systemNotificationService) {
        return new GoalReachedOutcomeAspectFactory(systemNotificationService);
    }

}
