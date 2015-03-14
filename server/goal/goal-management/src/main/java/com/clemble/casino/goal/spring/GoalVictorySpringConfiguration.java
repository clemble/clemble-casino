package com.clemble.casino.goal.spring;

import com.clemble.casino.goal.controller.GoalVictoryServiceController;
import com.clemble.casino.goal.listener.SystemGoalReachedVictoryEventListener;
import com.clemble.casino.goal.repository.GoalVictoryRepository;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created by mavarazy on 3/14/15.
 */

@Configuration
@Import({
    CommonSpringConfiguration.class,
    MongoSpringConfiguration.class
})
public class GoalVictorySpringConfiguration implements SpringConfiguration {

    @Bean
    public GoalVictoryRepository goalVictoryRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(GoalVictoryRepository.class);
    }

    @Bean
    public GoalVictoryServiceController goalVictoryServiceController(GoalVictoryRepository victoryRepository) {
        return new GoalVictoryServiceController(victoryRepository);
    }

    @Bean
    public SystemGoalReachedVictoryEventListener systemGoalReachedVictoryEventListener(
        SystemNotificationServiceListener notificationServiceListener,
        GoalVictoryRepository victoryRepository
    ) {
        SystemGoalReachedVictoryEventListener eventListener = new SystemGoalReachedVictoryEventListener(victoryRepository);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

}
