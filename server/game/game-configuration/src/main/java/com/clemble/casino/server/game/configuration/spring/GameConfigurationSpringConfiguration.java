package com.clemble.casino.server.game.configuration.spring;

import com.clemble.casino.server.game.configuration.controller.GameConfigurationController;
import com.clemble.casino.server.game.configuration.repository.GameConfigurationRepository;
import com.clemble.casino.server.game.configuration.service.ServerGameConfigurationService;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created by mavarazy on 8/30/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class, MongoSpringConfiguration.class})
public class GameConfigurationSpringConfiguration {

    @Bean
    public GameConfigurationRepository serverGameConfigurationRepository(MongoRepositoryFactory mongoRepositoryFactory) {
        return mongoRepositoryFactory.getRepository(GameConfigurationRepository.class);
    }

    @Bean
    public ServerGameConfigurationService serverGameConfigurationService(GameConfigurationRepository configurationRepository) {
        return new ServerGameConfigurationService(configurationRepository);
    }

    @Bean
    public GameConfigurationController gameConfigurationController(ServerGameConfigurationService configurationService) {
        return new GameConfigurationController(configurationService);
    }

}
