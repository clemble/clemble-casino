package com.gogomaya.server.spring.web;

import javax.inject.Inject;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.BaseUriMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaValidationService;
import com.gogomaya.server.game.configuration.TicTacToeConfigurationManager;
import com.gogomaya.server.game.connection.GameNotificationManager;
import com.gogomaya.server.game.match.TicTacToeSpecificationRepository;
import com.gogomaya.server.game.match.TicTacToeStateManager;
import com.gogomaya.server.game.session.TicTacToeSessionRepository;
import com.gogomaya.server.game.table.TicTacToeTableRepository;
import com.gogomaya.server.game.tictactoe.action.TicTacToeEngine;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.PlayerProfileRepository;
import com.gogomaya.server.player.SocialConnectionData;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerCredentialRepository;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.security.PlayerIdentityRepository;
import com.gogomaya.server.player.web.RegistrationRequest;
import com.gogomaya.server.social.SocialConnectionDataAdapter;
import com.gogomaya.server.web.GenericSchemaController;
import com.gogomaya.server.web.active.session.GameController;
import com.gogomaya.server.web.active.session.SessionController;
import com.gogomaya.server.web.error.GogomayaHandlerExceptionResolver;
import com.gogomaya.server.web.game.configuration.GameConfiguartionManagerController;
import com.gogomaya.server.web.registration.RegistrationLoginController;
import com.gogomaya.server.web.registration.RegistrationSignInContoller;
import com.gogomaya.server.web.registration.RegistrationSocialConnectionController;

@Configuration
public class WebMvcSpiConfiguration extends WebMvcConfigurationSupport {

    @Inject
    SocialConnectionDataAdapter connectionDataAdapter;

    @Inject
    PlayerProfileRepository playerProfileRepository;

    @Inject
    PlayerCredentialRepository playerCredentialRepository;

    @Inject
    PlayerIdentityRepository playerIdentityRepository;

    @Inject
    GogomayaValidationService validationService;

    @Inject
    TicTacToeConfigurationManager configurationManager;

    @Inject
    TicTacToeStateManager stateManager;

    @Inject
    TicTacToeSessionRepository sessionRepository;

    @Inject
    TicTacToeSpecificationRepository specificationRepository;

    @Inject
    TicTacToeTableRepository tableRepository;

    @Inject
    TicTacToeEngine engine;

    @Inject
    GameNotificationManager notificationManager;

    @Inject
    ObjectMapper objectMapper;

    @Bean
    public MappingJacksonHttpMessageConverter jacksonHttpMessageConverter() {
        MappingJacksonHttpMessageConverter messageConverter = new MappingJacksonHttpMessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        return messageConverter;
    }

    @Bean
    public RegistrationSocialConnectionController registrationSocialConnectionController() {
        return new RegistrationSocialConnectionController(connectionDataAdapter, playerIdentityRepository, validationService);
    }

    @Bean
    public RegistrationSignInContoller registrationSignInContoller() {
        return new RegistrationSignInContoller(playerProfileRepository, playerCredentialRepository, playerIdentityRepository, validationService);
    }

    @Bean
    public RegistrationLoginController registrationLoginController() {
        return new RegistrationLoginController(playerCredentialRepository, playerIdentityRepository);
    }

    @Bean
    public BaseUriMethodArgumentResolver baseUriMethodArgumentResolver() {
        return new BaseUriMethodArgumentResolver();
    }

    @Bean
    public GenericSchemaController jsonSchemaController() {
        GenericSchemaController genericSchemaController = new GenericSchemaController();
        genericSchemaController.addSchemaMapping("profile", PlayerProfile.class);
        genericSchemaController.addSchemaMapping("social", SocialConnectionData.class);
        genericSchemaController.addSchemaMapping("identity", PlayerIdentity.class);
        genericSchemaController.addSchemaMapping("credentials", PlayerCredential.class);
        genericSchemaController.addSchemaMapping("registration", RegistrationRequest.class);
        genericSchemaController.addSchemaMapping("error", GogomayaError.class);
        return genericSchemaController;
    }

    @Bean
    public HandlerExceptionResolver handlerExceptionResolver() {
        return new GogomayaHandlerExceptionResolver(objectMapper);
    }

    @Bean
    public SessionController sessionController() {
        return new SessionController(stateManager, sessionRepository, configurationManager);
    }

    @Bean
    public GameConfiguartionManagerController gameOptionsController() {
        return new GameConfiguartionManagerController(configurationManager);
    }

    @Bean
    public GameController gameController() {
        return new GameController(tableRepository, engine, notificationManager);
    }

}
