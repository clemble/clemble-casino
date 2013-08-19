package com.gogomaya.server.spring.web.player;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.rest.webmvc.BaseUriMethodArgumentResolver;

import com.gogomaya.server.error.GogomayaValidationService;
import com.gogomaya.server.player.notification.PlayerNotificationRegistry;
import com.gogomaya.server.player.registration.PlayerRegistrationService;
import com.gogomaya.server.player.state.PlayerStateManager;
import com.gogomaya.server.repository.player.PlayerCredentialRepository;
import com.gogomaya.server.repository.player.PlayerIdentityRepository;
import com.gogomaya.server.repository.player.PlayerProfileRepository;
import com.gogomaya.server.repository.player.PlayerSessionRepository;
import com.gogomaya.server.social.SocialConnectionDataAdapter;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.social.SocialModuleSpringConfiguration;
import com.gogomaya.server.spring.web.WebCommonSpringConfiguration;
import com.gogomaya.server.spring.web.SwaggerSpringConfiguration;
import com.gogomaya.server.web.player.PlayerProfileController;
import com.gogomaya.server.web.player.PlayerSessionController;
import com.gogomaya.server.web.player.registration.PlayerLoginController;
import com.gogomaya.server.web.player.registration.RegistrationSignInContoller;
import com.gogomaya.server.web.player.registration.RegistrationSocialConnectionController;
import com.mangofactory.swagger.SwaggerConfiguration;
import com.mangofactory.swagger.configuration.DefaultConfigurationModule;
import com.mangofactory.swagger.configuration.ExtensibilityModule;

@Configuration
@Import(value = {
        PlayerWebSpringConfiguration.PlayerDefaultAndTest.class,
        SocialModuleSpringConfiguration.class,
        WebCommonSpringConfiguration.class })
public class PlayerWebSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("socialConnectionDataAdapter")
    public SocialConnectionDataAdapter socialConnectionDataAdapter;

    @Autowired
    @Qualifier("playerProfileRepository")
    public PlayerProfileRepository playerProfileRepository;

    @Autowired
    @Qualifier("playerCredentialRepository")
    public PlayerCredentialRepository playerCredentialRepository;

    @Autowired
    @Qualifier("playerIdentityRepository")
    public PlayerIdentityRepository playerIdentityRepository;

    @Autowired
    @Qualifier("playerRegistrationService")
    public PlayerRegistrationService playerRegistrationService;

    @Autowired
    @Qualifier("playerSessionRepository")
    public PlayerSessionRepository playerSessionRepository;

    @Autowired
    @Qualifier("playerNotificationRegistry")
    public PlayerNotificationRegistry playerNotificationRegistry;

    @Autowired
    @Qualifier("gogomayaValidationService")
    public GogomayaValidationService gogomayaValidationService;

    @Autowired
    @Qualifier("playerStateManager")
    public PlayerStateManager playerStateManager;

    @Bean
    @Singleton
    public PlayerProfileController playerProfileController() {
        return new PlayerProfileController(playerProfileRepository);
    }

    @Bean
    @Singleton
    public RegistrationSocialConnectionController registrationSocialConnectionController() {
        return new RegistrationSocialConnectionController(socialConnectionDataAdapter, playerIdentityRepository, gogomayaValidationService);
    }

    @Bean
    @Singleton
    public RegistrationSignInContoller registrationSignInContoller() {
        return new RegistrationSignInContoller(playerRegistrationService, gogomayaValidationService);
    }

    @Bean
    @Singleton
    public PlayerLoginController registrationLoginController() {
        return new PlayerLoginController(playerCredentialRepository, playerIdentityRepository);
    }

    @Bean
    @Singleton
    public PlayerSessionController playerSessionController() {
        return new PlayerSessionController(playerNotificationRegistry, playerSessionRepository, playerStateManager);
    }

    @Bean
    @Singleton
    public BaseUriMethodArgumentResolver baseUriMethodArgumentResolver() {
        return new BaseUriMethodArgumentResolver();
    }
    
    @Configuration
    @Profile(value = { SpringConfiguration.PROFILE_DEFAULT, SpringConfiguration.PROFILE_TEST })
    public static class PlayerDefaultAndTest extends SwaggerSpringConfiguration {

        @Override
        public SwaggerConfiguration swaggerConfiguration(DefaultConfigurationModule defaultConfig, ExtensibilityModule extensibility) {
            SwaggerConfiguration swaggerConfiguration = new SwaggerConfiguration("1.0", "http://localhost:8080/player-web/");
            return extensibility.apply(defaultConfig.apply(swaggerConfiguration));
        }

    }

}
