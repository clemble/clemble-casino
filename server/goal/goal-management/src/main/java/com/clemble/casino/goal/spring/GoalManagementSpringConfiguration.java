package com.clemble.casino.goal.spring;

import com.clemble.casino.goal.action.GoalManagerFactoryFacade;
import com.clemble.casino.goal.aspect.GoalAspectFactory;
import com.clemble.casino.goal.aspect.notification.GoalPlayerNotificationAspectFactory;
import com.clemble.casino.goal.aspect.outcome.GoalLostOutcomeAspectFactory;
import com.clemble.casino.goal.aspect.outcome.GoalWonOutcomeAspectFactory;
import com.clemble.casino.goal.aspect.persistence.GoalStatePersistenceAspectFactory;
import com.clemble.casino.goal.aspect.record.GoalRecordAspectFactory;
import com.clemble.casino.goal.aspect.time.GoalTimeAspectFactory;
import com.clemble.casino.goal.controller.GoalActionServiceController;
import com.clemble.casino.goal.controller.GoalRecordServiceController;
import com.clemble.casino.goal.controller.FriendGoalServiceController;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.listener.SystemGoalStartedEventListener;
import com.clemble.casino.goal.listener.SystemGoalTimeoutEventListener;
import com.clemble.casino.goal.repository.GoalRecordRepository;
import com.clemble.casino.goal.repository.GoalStateRepository;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.action.ClembleManagerFactory;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created by mavarazy on 9/12/14.
 */
@Configuration
@Import({
    CommonSpringConfiguration.class,
    MongoSpringConfiguration.class
})
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
    public GoalActionServiceController goalActionServiceController(
        GoalManagerFactoryFacade factoryFacade,
        GoalStateRepository goalStateRepository) {
        return new GoalActionServiceController(factoryFacade, goalStateRepository);
    }

    @Bean
    public FriendGoalServiceController timelineServiceController(
        GoalStateRepository goalStateRepository,
        @Qualifier("playerConnectionClient") PlayerConnectionService playerConnectionClient
    ) {
        return new FriendGoalServiceController(goalStateRepository, playerConnectionClient);
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
        @Qualifier("playerNotificationService") ServerNotificationService notificationService) {
        return new GoalManagerFactoryFacade(goalManagerFactory, recordRepository, goalStateRepository, notificationService);
    }

    @Bean
    public SystemGoalStartedEventListener systemGoalStartedEventListener(
        @Qualifier("playerNotificationService") ServerNotificationService notificationService,
        SystemNotificationServiceListener notificationServiceListener,
        GoalManagerFactoryFacade goalManagerFactoryFacade) {
        SystemGoalStartedEventListener eventListener = new SystemGoalStartedEventListener(notificationService, goalManagerFactoryFacade);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Bean
    public SystemGoalTimeoutEventListener systemGoalTimeoutEventListener(
        SystemNotificationServiceListener notificationServiceListener,
        GoalManagerFactoryFacade goalManagerFactoryFacade) {
        SystemGoalTimeoutEventListener eventListener = new SystemGoalTimeoutEventListener(goalManagerFactoryFacade);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Bean
    public GoalRecordAspectFactory goalRecordAspectFactory(
        GoalRecordRepository recordRepository,
        @Qualifier("playerNotificationService") ServerNotificationService notificationService){
        return new GoalRecordAspectFactory(recordRepository, notificationService);
    }

    @Bean
    public GoalTimeAspectFactory goalTimeAspectFactory(SystemNotificationService systemNotificationService){
        return new GoalTimeAspectFactory(systemNotificationService);
    }

    @Bean
    public GoalStatePersistenceAspectFactory goalPersistenceAspectFactory(GoalStateRepository stateRepository) {
        return new GoalStatePersistenceAspectFactory(stateRepository);
    }

    @Bean
    public GoalLostOutcomeAspectFactory goalMissedOutcomeAspectFactory(SystemNotificationService systemNotificationService) {
        return new GoalLostOutcomeAspectFactory(systemNotificationService);
    }

    @Bean
    public GoalWonOutcomeAspectFactory goalReachedOutcomeAspectFactory(SystemNotificationService systemNotificationService) {
        return new GoalWonOutcomeAspectFactory(systemNotificationService);
    }

    @Bean
    public GoalPlayerNotificationAspectFactory goalPlayerNotificationAspectFactory(
        @Qualifier("playerNotificationService") ServerNotificationService notificationService) {
        return new GoalPlayerNotificationAspectFactory(notificationService);
    }

}
