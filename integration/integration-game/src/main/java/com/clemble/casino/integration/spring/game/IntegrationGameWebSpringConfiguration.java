package com.clemble.casino.integration.spring.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.game.Game;
import com.clemble.casino.integration.game.NumberState;
import com.clemble.casino.integration.game.NumberStateFactory;
import com.clemble.casino.server.game.action.GameProcessorFactory;
import com.clemble.casino.server.game.action.GameStateFactory;
import com.clemble.casino.server.repository.game.GameConstructionRepository;
import com.clemble.casino.server.spring.game.GameManagementSpringConfiguration;
import com.clemble.casino.server.spring.web.WebCommonSpringConfiguration;
import com.clemble.casino.server.spring.web.game.AbstractGameSpringConfiguration;

@Configuration
@Import({ GameManagementSpringConfiguration.class, WebCommonSpringConfiguration.class })
public class IntegrationGameWebSpringConfiguration extends AbstractGameSpringConfiguration<NumberState> {

    @Autowired
    public GameConstructionRepository constructionRepository;

    @Autowired
    public GameProcessorFactory<NumberState> processorFactory;

    @Override
    public Game getGame() {
        return Game.num;
    }

    @Bean
    @Override
    public GameStateFactory<NumberState> ticTacToeStateFactory() {
        return new NumberStateFactory(constructionRepository, processorFactory);
    }

}
