package com.clemble.casino.goal.spring;

import com.clemble.casino.goal.action.GoalManagerFactory;
import com.clemble.casino.goal.controller.GoalRecordServiceController;
import com.clemble.casino.goal.lisetener.SystemGoalStartedEventListener;
import com.clemble.casino.goal.repository.GoalRecordRepository;
import com.clemble.casino.server.player.notification.SystemNotificationService;
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
    public GoalManagerFactory goalManagerFactory(GoalRecordRepository recordRepository) {
        return new GoalManagerFactory(recordRepository);
    }

    @Bean
    public SystemGoalStartedEventListener systemGoalStartedEventListener(
        SystemNotificationServiceListener notificationServiceListener,
        GoalManagerFactory managerFactory) {
        SystemGoalStartedEventListener eventListener = new SystemGoalStartedEventListener(managerFactory);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

}
