package com.gogomaya.server.spring.web;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.error.GogomayaValidationService;
import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.game.action.GameStateFactory;
import com.gogomaya.server.game.match.GameConstructionService;
import com.gogomaya.server.game.notification.GameNotificationService;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.specification.GameSpecificationRepository;
import com.gogomaya.server.game.table.PendingSessionQueue;
import com.gogomaya.server.game.table.GameTableRepository;
import com.gogomaya.server.spring.tictactoe.TicTacToeSpringConfiguration;
import com.gogomaya.server.tictactoe.TicTacToeState;
import com.gogomaya.server.tictactoe.configuration.TicTacToeConfigurationManager;
import com.gogomaya.server.web.active.session.GameConstructionController;
import com.gogomaya.server.web.active.session.GameEngineController;
import com.gogomaya.server.web.error.GogomayaHandlerExceptionResolver;
import com.gogomaya.server.web.game.configuration.GameConfigurationManagerController;

@Configuration
@Import(value = { TicTacToeSpringConfiguration.class })
public class TicTacToeWebSpringConfiguration extends WebMvcConfigurationSupport {

    @Inject
    GogomayaValidationService validationService;

    @Inject
    TicTacToeConfigurationManager configurationManager;

    @Inject
    GameSessionProcessor<TicTacToeState> sessionProcessor;

    @Inject
    GameConstructionService<TicTacToeState> stateManager;

    @Inject
    GameSessionRepository<TicTacToeState> sessionRepository;

    @Inject
    GameSpecificationRepository specificationRepository;

    @Inject
    GameTableRepository<TicTacToeState> tableRepository;

    @Inject
    GameNotificationService<TicTacToeState> notificationManager;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    PendingSessionQueue tableQueue;

    @Inject
    GameStateFactory<TicTacToeState> stateFactory;

    @Bean
    @Singleton
    public MappingJackson2HttpMessageConverter jacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        return messageConverter;
    }

    @Bean
    @Singleton
    public HandlerExceptionResolver handlerExceptionResolver() {
        return new GogomayaHandlerExceptionResolver(objectMapper);
    }

    @Bean
    @Singleton
    public GameConstructionController<TicTacToeState> gameTableMatchController() {
        return new GameConstructionController<TicTacToeState>(stateManager, tableRepository, configurationManager);
    }

    @Bean
    @Singleton
    public GameConfigurationManagerController gameOptionsController() {
        return new GameConfigurationManagerController(configurationManager);
    }

    @Bean
    @Singleton
    public GameEngineController<TicTacToeState> gameController() {
        return new GameEngineController<TicTacToeState>(sessionProcessor, tableRepository);
    }

}
