package com.gogomaya.server.spring.web.player;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.rest.webmvc.BaseUriMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.error.GogomayaValidationService;
import com.gogomaya.server.player.PlayerProfileRepository;
import com.gogomaya.server.player.notification.PlayerNotificationRegistry;
import com.gogomaya.server.player.registration.PlayerRegistrationService;
import com.gogomaya.server.player.security.PlayerCredentialRepository;
import com.gogomaya.server.player.security.PlayerIdentityRepository;
import com.gogomaya.server.player.session.PlayerSessionRepository;
import com.gogomaya.server.player.state.PlayerStateManager;
import com.gogomaya.server.player.state.RedisPlayerStateManager;
import com.gogomaya.server.social.SocialConnectionDataAdapter;
import com.gogomaya.server.spring.social.SocialModuleSpringConfiguration;
import com.gogomaya.server.web.error.GogomayaHandlerExceptionResolver;
import com.gogomaya.server.web.player.registration.RegistrationLoginController;
import com.gogomaya.server.web.player.registration.RegistrationSignInContoller;
import com.gogomaya.server.web.player.registration.RegistrationSocialConnectionController;
import com.gogomaya.server.web.player.session.PlayerSessionController;

@Configuration
@Import(value = { SocialModuleSpringConfiguration.class })
public class PlayerWebSpringConfiguration extends WebMvcConfigurationSupport {

    @Inject
    public SocialConnectionDataAdapter connectionDataAdapter;

    @Inject
    public PlayerProfileRepository playerProfileRepository;

    @Inject
    public PlayerCredentialRepository playerCredentialRepository;

    @Inject
    public PlayerIdentityRepository playerIdentityRepository;

    @Inject
    public PlayerRegistrationService playerRegistrationService;

    @Inject
    public PlayerSessionRepository playerSessionRepository;

    @Inject
    public PlayerNotificationRegistry notificationRegistry;

    @Inject
    public GogomayaValidationService validationService;

    @Inject
    public ObjectMapper objectMapper;

    @Inject
    @Named("playerQueueTemplate")
    public RedisTemplate<Long, Long> playerQueueTemplate;

    @Bean
    @Singleton
    public PlayerStateManager playerStateManager() {
        return new RedisPlayerStateManager(playerQueueTemplate);
    }

    @Bean
    @Singleton
    public MappingJackson2HttpMessageConverter jacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        return messageConverter;
    }

    @Bean
    @Singleton
    public RegistrationSocialConnectionController registrationSocialConnectionController() {
        return new RegistrationSocialConnectionController(connectionDataAdapter, playerIdentityRepository, validationService);
    }

    @Bean
    @Singleton
    public RegistrationSignInContoller registrationSignInContoller() {
        return new RegistrationSignInContoller(playerRegistrationService, validationService);
    }

    @Bean
    @Singleton
    public RegistrationLoginController registrationLoginController() {
        return new RegistrationLoginController(playerCredentialRepository, playerIdentityRepository);
    }

    @Bean
    @Singleton
    public PlayerSessionController sessionController() {
        return new PlayerSessionController(notificationRegistry, playerSessionRepository, playerStateManager());
    }

    @Bean
    @Singleton
    public BaseUriMethodArgumentResolver baseUriMethodArgumentResolver() {
        return new BaseUriMethodArgumentResolver();
    }

    @Bean
    @Singleton
    public HandlerExceptionResolver handlerExceptionResolver() {
        return new GogomayaHandlerExceptionResolver(objectMapper);
    }

}
