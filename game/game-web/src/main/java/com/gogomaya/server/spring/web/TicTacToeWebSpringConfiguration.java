package com.gogomaya.server.spring.web;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.game.construct.GameConstructionService;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.table.GameTableRepository;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.spring.tictactoe.TicTacToeSpringConfiguration;
import com.gogomaya.server.tictactoe.configuration.TicTacToeConfigurationManager;
import com.gogomaya.server.web.active.session.GameConstructionController;
import com.gogomaya.server.web.active.session.GameEngineController;
import com.gogomaya.server.web.game.configuration.GameConfigurationManagerController;

@Configuration
@Import(value = { TicTacToeSpringConfiguration.class, CommonWebSpringConfiguration.class })
public class TicTacToeWebSpringConfiguration {

    @Inject
    GameConstructionService constructionService;

    @Inject
    TicTacToeConfigurationManager configurationManager;

    @Inject
    GameSessionProcessor<TicTacToeState> sessionProcessor;

    @Inject
    GameSessionRepository<TicTacToeState> sessionRepository;

    @Inject
    GameTableRepository<TicTacToeState> tableRepository;

    @Bean
    @Singleton
    public GameConstructionController<TicTacToeState> gameTableMatchController() {
        return new GameConstructionController<TicTacToeState>(constructionService, configurationManager);
    }

    @Bean
    @Singleton
    public GameConfigurationManagerController gameOptionsController() {
        return new GameConfigurationManagerController(configurationManager);
    }

    @Bean
    @Singleton
    public GameEngineController<TicTacToeState> gameController() {
        return new GameEngineController<TicTacToeState>(sessionProcessor);
    }

}
