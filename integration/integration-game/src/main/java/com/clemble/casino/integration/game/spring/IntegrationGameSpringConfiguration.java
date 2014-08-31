package com.clemble.casino.integration.game.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.integration.game.NumberState;
import com.clemble.casino.integration.game.NumberStateFactory;
import com.clemble.casino.game.GameStateFactory;
import com.clemble.casino.server.game.spring.GameManagementSpringConfiguration;
import com.clemble.casino.server.spring.web.game.AbstractGameSpringConfiguration;

@Configuration
@Import({ GameManagementSpringConfiguration.class})
public class IntegrationGameSpringConfiguration extends AbstractGameSpringConfiguration<NumberState> {


    @Bean
    public GameStateFactory<NumberState> gameStateFactory() {
        return new NumberStateFactory();
    }

}
