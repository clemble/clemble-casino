package com.clemble.casino.server.spring.web.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.error.GogomayaValidationService;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;
import com.clemble.casino.server.player.registration.PlayerProfileRegistrationServerService;
import com.clemble.casino.server.player.registration.SimplePlayerProfileRegistrationServerService;
import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.clemble.casino.server.social.SocialConnectionDataAdapter;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.social.SocialModuleSpringConfiguration;
import com.clemble.casino.server.spring.web.WebCommonSpringConfiguration;
import com.clemble.casino.server.web.player.PlayerPresenceController;
import com.clemble.casino.server.web.player.PlayerProfileController;
import com.clemble.casino.server.web.player.registration.PlayerProfileRegistrationController;

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
    @Qualifier("gogomayaValidationService")
    public GogomayaValidationService gogomayaValidationService;

    @Autowired
    @Qualifier("playerStateManager")
    public PlayerPresenceServerService playerPresenceServerService;

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
    public PlayerPresenceController playerPresenceController() {
        return new PlayerPresenceController(playerPresenceServerService);
    }

}