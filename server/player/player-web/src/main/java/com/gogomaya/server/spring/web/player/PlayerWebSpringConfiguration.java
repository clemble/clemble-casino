package com.gogomaya.server.spring.web.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.error.GogomayaValidationService;
import com.gogomaya.server.player.notification.PlayerNotificationRegistry;
import com.gogomaya.server.player.registration.PlayerProfileRegistrationServerService;
import com.gogomaya.server.player.registration.SimplePlayerProfileRegistrationServerService;
import com.gogomaya.server.player.state.PlayerStateManager;
import com.gogomaya.server.repository.player.PlayerProfileRepository;
import com.gogomaya.server.repository.player.PlayerSessionRepository;
import com.gogomaya.server.social.SocialConnectionDataAdapter;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.social.SocialModuleSpringConfiguration;
import com.gogomaya.server.spring.web.WebCommonSpringConfiguration;
import com.gogomaya.server.web.player.PlayerProfileController;
import com.gogomaya.server.web.player.PlayerSessionController;
import com.gogomaya.server.web.player.registration.PlayerProfileRegistrationController;

@Configuration
@Import(value = { SocialModuleSpringConfiguration.class, WebCommonSpringConfiguration.class })
public class PlayerWebSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("socialConnectionDataAdapter")
    public SocialConnectionDataAdapter socialConnectionDataAdapter;

    @Autowired
    @Qualifier("playerProfileRepository")
    public PlayerProfileRepository playerProfileRepository;

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
    public PlayerProfileController playerProfileController() {
        return new PlayerProfileController(playerProfileRepository);
    }

    @Bean
    public PlayerProfileRegistrationServerService realPlayerProfileRegistrationService() {
        return new SimplePlayerProfileRegistrationServerService(gogomayaValidationService, playerProfileRepository, socialConnectionDataAdapter);
    }

    @Bean
    public PlayerProfileRegistrationController playerProfileRegistrationController() {
        return new PlayerProfileRegistrationController(realPlayerProfileRegistrationService());
    }

    @Bean
    public PlayerSessionController playerSessionController() {
        return new PlayerSessionController(playerNotificationRegistry, playerSessionRepository, playerStateManager);
    }

}
