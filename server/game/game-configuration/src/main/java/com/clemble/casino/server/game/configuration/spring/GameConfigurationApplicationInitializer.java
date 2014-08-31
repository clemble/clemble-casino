package com.clemble.casino.server.game.configuration.spring;

import com.clemble.casino.server.game.configuration.controller.GameConfigurationController;
import com.clemble.casino.server.game.configuration.repository.ServerGameConfigurationRepository;
import com.clemble.casino.server.game.configuration.service.ServerGameConfigurationService;
import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created by mavarazy on 8/30/14.
 */
public class GameConfigurationApplicationInitializer extends AbstractWebApplicationInitializer {

    public GameConfigurationApplicationInitializer(){
        super(GameConfigurationSpringConfiguration.class);
    }

}
