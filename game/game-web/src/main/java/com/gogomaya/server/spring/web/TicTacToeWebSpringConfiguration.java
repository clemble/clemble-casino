package com.gogomaya.server.spring.web;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.game.configuration.GameSpecificationRegistry;
import com.gogomaya.server.game.construct.GameConstructionService;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.repository.game.GameConstructionRepository;
import com.gogomaya.server.repository.game.GameSessionRepository;
import com.gogomaya.server.repository.game.GameTableRepository;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.tictactoe.TicTacToeSpringConfiguration;
import com.gogomaya.server.tictactoe.configuration.TicTacToeConfigurationManager;
import com.gogomaya.server.web.active.session.GameConstructionController;
import com.gogomaya.server.web.active.session.GameActionController;
import com.gogomaya.server.web.game.configuration.GameConfigurationManagerController;

@Configuration
@Import(value = { TicTacToeSpringConfiguration.class, CommonWebSpringConfiguration.class })
public class TicTacToeWebSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("gameSessionRepository")
    public GameSessionRepository<TicTacToeState> gameSessionRepository;

    @Autowired
    @Qualifier("gameTableRepository")
    public GameTableRepository<TicTacToeState> tableRepository;

    @Autowired
    @Qualifier("gameConstructionRepository")
    public GameConstructionRepository constructionRepository;

    @Autowired
    @Qualifier("ticTacToeConstructionService")
    public GameConstructionService constructionService;

    @Autowired
    @Qualifier("ticTacToeConfigurationManager")
    public TicTacToeConfigurationManager ticTacToeConfigurationManager;

    @Autowired
    @Qualifier("ticTacToeSessionProcessor")
    public GameSessionProcessor<TicTacToeState> ticTacToeSessionProcessor;

    @Autowired
    public GameSpecificationRegistry gameSpecificationRegistry;

    @Bean
    @Singleton
    public GameConstructionController<TicTacToeState> ticTacToeConstructionController() {
        return new GameConstructionController<TicTacToeState>(constructionRepository, constructionService, gameSpecificationRegistry);
    }

    @Bean
    @Singleton
    public GameConfigurationManagerController ticTacToeConfigurationManagerController() {
        return new GameConfigurationManagerController(gameSpecificationRegistry);
    }

    @Bean
    @Singleton
    public GameActionController<TicTacToeState> ticTacToeEngineController() {
        return new GameActionController<TicTacToeState>(ticTacToeSessionProcessor);
    }

}
