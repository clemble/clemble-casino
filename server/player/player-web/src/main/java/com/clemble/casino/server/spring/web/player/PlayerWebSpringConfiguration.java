package com.clemble.casino.server.spring.web.player;

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
import com.clemble.casino.server.social.SocialConnectionDataAdapter;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.social.PlayerSocialSpringConfiguration;
import com.clemble.casino.server.spring.web.WebCommonSpringConfiguration;
import com.clemble.casino.server.web.player.PlayerPresenceController;

@Configuration
@Import(value = {
    PlayerSocialSpringConfiguration.class,
    WebCommonSpringConfiguration.class,
    ManagementWebSpringConfiguration.class,
})
public class PlayerWebSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerPresenceController playerPresenceController(ServerPlayerPresenceService playerPresenceServerService) {
        return new PlayerPresenceController(playerPresenceServerService);
    }

}
