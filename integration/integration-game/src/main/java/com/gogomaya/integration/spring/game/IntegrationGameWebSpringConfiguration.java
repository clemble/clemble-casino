package com.gogomaya.integration.spring.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.game.Game;
import com.gogomaya.server.game.action.GameProcessorFactory;
import com.gogomaya.server.game.action.GameStateFactory;
import com.gogomaya.server.integration.NumberState;
import com.gogomaya.server.integration.NumberStateFactory;
import com.gogomaya.server.repository.game.GameConstructionRepository;
import com.gogomaya.server.spring.game.GameManagementSpringConfiguration;
import com.gogomaya.server.spring.web.WebCommonSpringConfiguration;
import com.gogomaya.server.spring.web.game.AbstractGameSpringConfiguration;

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
