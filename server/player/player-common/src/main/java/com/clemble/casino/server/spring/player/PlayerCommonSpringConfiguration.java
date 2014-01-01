package com.clemble.casino.server.spring.player;

import com.clemble.casino.server.player.registration.ServerProfileRegistrationService;
import com.clemble.casino.server.player.registration.RestServerProfileRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.PlayerPresenceSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.player.PlayerCommonSpringConfiguration.IntegrationPlayerConfiguration;
import com.clemble.casino.server.spring.player.PlayerCommonSpringConfiguration.TestPlayerConfiguration;
import com.clemble.casino.server.spring.web.ClientRestCommonSpringConfiguration;

@Configuration
@Import({ CommonSpringConfiguration.class,
    PlayerCouchbaseSpringConfiguration.class,
    IntegrationPlayerConfiguration.class,
    TestPlayerConfiguration.class,
    PlayerPresenceSpringConfiguration.class })
public class PlayerCommonSpringConfiguration implements SpringConfiguration {

    @Import(ClientRestCommonSpringConfiguration.class)
    @Profile(value = { DEFAULT, INTEGRATION_TEST, INTEGRATION_CLOUD, CLOUD })
    public static class IntegrationPlayerConfiguration {

        @Autowired(required = false)
        @Qualifier("realPlayerProfileRegistrationService")
        public ServerProfileRegistrationService realPlayerProfileRegistrationService;

        public String getBaseUrl() {
            return "http://127.0.0.1:8080/player/";
        }

        @Bean
        @Autowired
        public ServerProfileRegistrationService playerProfileRegistrationService(RestTemplate restTemplate) {
            return realPlayerProfileRegistrationService == null ? new RestServerProfileRegistrationService(getBaseUrl(), restTemplate) : realPlayerProfileRegistrationService;
        }

    }

    @Configuration
    @Profile(value = { UNIT_TEST })
    public static class TestPlayerConfiguration {

        @Bean
        public ServerProfileRegistrationService playerProfileRegistrationService() {
            return new ServerProfileRegistrationService() {

                @Override
                public PlayerProfile create(SocialConnectionData socialConnectionData) {
                    return null;
                }

                @Override
                public PlayerProfile create(PlayerProfile playerProfile) {
                    return playerProfile;
                }

                @Override
                public PlayerProfile create(SocialAccessGrant accessGrant) {
                    return null;
                }
            };
        }
    }

}
