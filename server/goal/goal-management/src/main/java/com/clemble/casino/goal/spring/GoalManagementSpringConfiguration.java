package com.clemble.casino.goal.spring;

import com.clemble.casino.goal.action.GoalManagerFactoryFacade;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.controller.GoalRecordServiceController;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lisetener.SystemGoalStartedEventListener;
import com.clemble.casino.goal.repository.GoalRecordRepository;
import com.clemble.casino.server.action.ClembleManagerFactory;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

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
    public GoalRecordServiceController goalRecordServiceController(GoalRecordRepository recordRepository) {
        return new GoalRecordServiceController(recordRepository);
    }

    @Bean
    public ClembleManagerFactory<GoalConfiguration> goalManagerFactory() {
        return new ClembleManagerFactory<>(GoalAspect.class);
    }

    @Bean
    public GoalManagerFactoryFacade goalManagerFactoryFacade(ClembleManagerFactory<GoalConfiguration> goalManagerFactory, GoalRecordRepository recordRepository) {
        return new GoalManagerFactoryFacade(goalManagerFactory, recordRepository);
    }

    @Bean
    public SystemGoalStartedEventListener systemGoalStartedEventListener(
        SystemNotificationServiceListener notificationServiceListener,
        GoalManagerFactoryFacade goalManagerFactoryFacade) {
        SystemGoalStartedEventListener eventListener = new SystemGoalStartedEventListener(goalManagerFactoryFacade);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

}
