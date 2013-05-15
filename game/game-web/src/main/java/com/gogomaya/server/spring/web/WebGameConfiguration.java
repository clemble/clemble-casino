package com.gogomaya.server.spring.web;

import javax.inject.Inject;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.gogomaya.server.error.GogomayaValidationService;
import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.game.action.GameStateFactory;
import com.gogomaya.server.game.configuration.TicTacToeConfigurationManager;
import com.gogomaya.server.game.connection.GameNotificationService;
import com.gogomaya.server.game.connection.GameServerConnectionManager;
import com.gogomaya.server.game.match.GameMatchingService;
import com.gogomaya.server.game.outcome.GameOutcomeService;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.specification.GameSpecificationRepository;
import com.gogomaya.server.game.table.GameTableQueue;
import com.gogomaya.server.game.table.GameTableRepository;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.spring.game.TicTacToeSpringConfiguration;
import com.gogomaya.server.web.active.session.GameEngineController;
import com.gogomaya.server.web.active.session.GameTableMatchController;
import com.gogomaya.server.web.error.GogomayaHandlerExceptionResolver;
import com.gogomaya.server.web.game.configuration.GameConfigurationManagerController;

@Configuration
@Import(value = { TicTacToeSpringConfiguration.class })
public class WebGameConfiguration extends WebMvcConfigurationSupport {

    @Inject
    GogomayaValidationService validationService;

    @Inject
    TicTacToeConfigurationManager configurationManager;
    
    @Inject
    GameSessionProcessor<TicTacToeState> sessionProcessor;

    @Inject
    GameMatchingService<TicTacToeState> stateManager;

    @Inject
    GameSessionRepository sessionRepository;

    @Inject
    GameSpecificationRepository specificationRepository;

    @Inject
    GameTableRepository<TicTacToeState> tableRepository;

    @Inject
    GameNotificationService<TicTacToeState> notificationManager;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    GameTableQueue<TicTacToeState> tableManager;

    @Inject
    GameServerConnectionManager serverConnectionManager;

    @Inject
    GameStateFactory<TicTacToeState> stateFactory;

    @Inject
    GameOutcomeService<TicTacToeState> outcomeService;

    @Bean
    public MappingJacksonHttpMessageConverter jacksonHttpMessageConverter() {
        MappingJacksonHttpMessageConverter messageConverter = new MappingJacksonHttpMessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        return messageConverter;
    }

    @Bean
    public HandlerExceptionResolver handlerExceptionResolver() {
        return new GogomayaHandlerExceptionResolver(objectMapper);
    }

    @Bean
    public GameTableMatchController<TicTacToeState> sessionController() {
        return new GameTableMatchController<TicTacToeState>(stateManager, tableRepository, configurationManager);
    }

    @Bean
    public GameConfigurationManagerController gameOptionsController() {
        return new GameConfigurationManagerController(configurationManager);
    }

    @Bean
    public GameEngineController<TicTacToeState> gameController() {
        return new GameEngineController<TicTacToeState>(sessionProcessor, tableRepository);
    }

}
