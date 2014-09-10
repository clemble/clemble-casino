package com.clemble.casino.goal.construction.spring;

import com.clemble.casino.goal.construction.controller.GoalConstructionServiceController;
import com.clemble.casino.goal.construction.repository.GoalConstructionRepository;
import com.clemble.casino.goal.construction.service.ServerGoalConstructionService;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created by mavarazy on 9/10/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class, MongoSpringConfiguration.class})
public class GoalConstructionSpringConfiguration {

    @Bean
    public GoalConstructionRepository goalConstructionRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(GoalConstructionRepository.class);
    }

    @Bean
    public ServerGoalConstructionService goalConstructionService(GoalConstructionRepository repository) {
        return new ServerGoalConstructionService(repository);
    }

    @Bean
    public GoalConstructionServiceController goalConstructionServiceController(ServerGoalConstructionService constructionService) {
        return new GoalConstructionServiceController(constructionService);
    }

}
