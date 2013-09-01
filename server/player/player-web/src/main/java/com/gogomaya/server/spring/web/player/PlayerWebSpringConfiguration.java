package com.gogomaya.server.spring.web.player;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.gogomaya.error.GogomayaValidationService;
import com.gogomaya.server.player.notification.PlayerNotificationRegistry;
import com.gogomaya.server.player.registration.PlayerRegistrationProcessingService;
import com.gogomaya.server.player.state.PlayerStateManager;
import com.gogomaya.server.repository.player.PlayerCredentialRepository;
import com.gogomaya.server.repository.player.PlayerIdentityRepository;
import com.gogomaya.server.repository.player.PlayerProfileRepository;
import com.gogomaya.server.repository.player.PlayerSessionRepository;
import com.gogomaya.server.social.SocialConnectionDataAdapter;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.social.SocialModuleSpringConfiguration;
import com.gogomaya.server.spring.web.SwaggerSpringConfiguration;
import com.gogomaya.server.spring.web.WebCommonSpringConfiguration;
import com.gogomaya.server.web.player.PlayerProfileController;
import com.gogomaya.server.web.player.PlayerSessionController;
import com.gogomaya.server.web.player.registration.PlayerRegistrationController;
import com.mangofactory.swagger.SwaggerConfiguration;
import com.mangofactory.swagger.configuration.DefaultConfigurationModule;
import com.mangofactory.swagger.configuration.ExtensibilityModule;

@Configuration
@Import(value = { PlayerWebSpringConfiguration.PlayerDefaultAndTest.class, SocialModuleSpringConfiguration.class, WebCommonSpringConfiguration.class })
public class PlayerWebSpringConfiguration implements SpringConfiguration {

    @Inject
    @Named("socialConnectionDataAdapter")
    public SocialConnectionDataAdapter socialConnectionDataAdapter;

    @Inject
    @Named("playerProfileRepository")
    public PlayerProfileRepository playerProfileRepository;

    @Inject
    @Named("playerCredentialRepository")
    public PlayerCredentialRepository playerCredentialRepository;

    @Inject
    @Named("playerIdentityRepository")
    public PlayerIdentityRepository playerIdentityRepository;

    @Inject
    @Named("playerRegistrationService")
    public PlayerRegistrationProcessingService playerRegistrationService;

    @Inject
    @Named("playerSessionRepository")
    public PlayerSessionRepository playerSessionRepository;

    @Inject
    @Named("playerNotificationRegistry")
    public PlayerNotificationRegistry playerNotificationRegistry;

    @Inject
    @Named("gogomayaValidationService")
    public GogomayaValidationService gogomayaValidationService;

    @Inject
    @Named("playerStateManager")
    public PlayerStateManager playerStateManager;

    @Bean
    @Singleton
    public PlayerProfileController playerProfileController() {
        return new PlayerProfileController(playerProfileRepository);
    }

    @Bean
    @Singleton
    public PlayerRegistrationController registrationLoginController() {
        return new PlayerRegistrationController(playerCredentialRepository, playerIdentityRepository, gogomayaValidationService, playerRegistrationService,
                socialConnectionDataAdapter);
    }

    @Bean
    @Singleton
    public PlayerSessionController playerSessionController() {
        return new PlayerSessionController(playerNotificationRegistry, playerSessionRepository, playerStateManager);
    }

    @Configuration
    @Profile(value = { DEFAULT, UNIT_TEST, INTEGRATION_TEST })
    public static class PlayerDefaultAndTest extends SwaggerSpringConfiguration {

        @Override
        public SwaggerConfiguration swaggerConfiguration(DefaultConfigurationModule defaultConfig, ExtensibilityModule extensibility) {
            SwaggerConfiguration swaggerConfiguration = new SwaggerConfiguration("1.0", "http://localhost:8080/player-web/");
            return extensibility.apply(defaultConfig.apply(swaggerConfiguration));
        }

    }

}
