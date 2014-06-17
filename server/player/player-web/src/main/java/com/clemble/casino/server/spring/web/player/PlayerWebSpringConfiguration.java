package com.clemble.casino.server.spring.web.player;

import com.clemble.casino.server.player.registration.BasicServerProfileRegistrationService;

import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.web.OAuthSpringConfiguration;
import com.clemble.casino.server.spring.web.management.ManagementWebSpringConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.clemble.casino.server.repository.player.PlayerSocialNetworkRepository;
import com.clemble.casino.server.social.SocialConnectionDataAdapter;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.social.PlayerSocialSpringConfiguration;
import com.clemble.casino.server.spring.web.WebCommonSpringConfiguration;
import com.clemble.casino.server.web.player.PlayerConnectionController;
import com.clemble.casino.server.web.player.PlayerPresenceController;
import com.clemble.casino.server.web.player.PlayerProfileController;

@Configuration
@Import(value = {
    PlayerSocialSpringConfiguration.class,
    WebCommonSpringConfiguration.class,
    ManagementWebSpringConfiguration.class,
})
public class PlayerWebSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerProfileController playerProfileControlle(PlayerProfileRepository playerProfileRepository) {
        return new PlayerProfileController(playerProfileRepository);
    }

    @Bean
    @Autowired
    public BasicServerProfileRegistrationService realPlayerProfileRegistrationService(
        ClembleCasinoValidationService clembleValidationService,
        PlayerProfileRepository playerProfileRepository,
        PlayerSocialNetworkRepository socialNetworkRepository,
        SocialConnectionDataAdapter socialConnectionDataAdapter) {
        return new BasicServerProfileRegistrationService(clembleValidationService, playerProfileRepository, socialConnectionDataAdapter, socialNetworkRepository);
    }

    @Bean
    public PlayerPresenceController playerPresenceController(ServerPlayerPresenceService playerPresenceServerService) {
        return new PlayerPresenceController(playerPresenceServerService);
    }

    @Bean
    public PlayerConnectionController playerConnectionController(PlayerSocialNetworkRepository socialNetworkRepository) {
        return new PlayerConnectionController(socialNetworkRepository);
    }

//
//    TODO Consider as deprecated, remove from tests
//    This controller is no longer needed, since Management and Player were merged
//    @Bean
//    public ServerProfileRegistrationController playerProfileRegistrationController(BasicServerProfileRegistrationService realPlayerProfileRegistrationService) {
//        return new ServerProfileRegistrationController(realPlayerProfileRegistrationService);
//    }

}
