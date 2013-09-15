package com.gogomaya.server.spring.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.player.PlayerProfile;
import com.gogomaya.player.SocialConnectionData;
import com.gogomaya.server.player.registration.PlayerProfileRegistrationServerService;
import com.gogomaya.server.player.registration.RestPlayerProfileRegistrationServerService;
import com.gogomaya.server.spring.common.CommonSpringConfiguration;
import com.gogomaya.server.spring.common.PlayerPresenceSpringConfiguration;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.player.PlayerCommonSpringConfiguration.IntegrationCloudPlayerConfiguration;
import com.gogomaya.server.spring.player.PlayerCommonSpringConfiguration.IntegrationPlayerConfiguration;
import com.gogomaya.server.spring.player.PlayerCommonSpringConfiguration.IntegrationTestPlayerConfiguration;
import com.gogomaya.server.spring.player.PlayerCommonSpringConfiguration.TestPlayerConfiguration;
import com.gogomaya.server.spring.web.ClientRestCommonSpringConfiguration;

@Configuration
@Import({ CommonSpringConfiguration.class,
    IntegrationCloudPlayerConfiguration.class,
    IntegrationTestPlayerConfiguration.class,
    IntegrationPlayerConfiguration.class,
    TestPlayerConfiguration.class,
    PlayerPresenceSpringConfiguration.class })
public class PlayerCommonSpringConfiguration implements SpringConfiguration {

    @Configuration
    @Profile(value = SpringConfiguration.INTEGRATION_CLOUD)
    public static class IntegrationCloudPlayerConfiguration extends IntegrationPlayerConfiguration {

        @Override
        public String getBaseUrl() {
            return "http://ec2-50-16-93-157.compute-1.amazonaws.com/gogomaya/";
        }

    }

    @Configuration
    @Profile(value = { SpringConfiguration.INTEGRATION_TEST })
    public static class IntegrationTestPlayerConfiguration extends IntegrationPlayerConfiguration {

        @Override
        public String getBaseUrl() {
            return "http://localhost:9999/";
        }

    }

    @Import(ClientRestCommonSpringConfiguration.class)
    @Profile(value = { DEFAULT })
    public static class IntegrationPlayerConfiguration {

        @Autowired(required = false)
        @Qualifier("realPlayerProfileRegistrationService")
        public PlayerProfileRegistrationServerService realPlayerProfileRegistrationService;

        public String getBaseUrl() {
            return "http://localhost:8080/";
        }

        @Bean
        @Autowired
        public PlayerProfileRegistrationServerService playerProfileRegistrationService(RestTemplate restTemplate) {
            return realPlayerProfileRegistrationService == null ? new RestPlayerProfileRegistrationServerService(getBaseUrl(), restTemplate)
                    : realPlayerProfileRegistrationService;
        }

    }

    @Configuration
    @Profile(value = { UNIT_TEST })
    public static class TestPlayerConfiguration {

        @Bean
        public PlayerProfileRegistrationServerService playerProfileRegistrationService() {
            return new PlayerProfileRegistrationServerService() {

                @Override
                public PlayerProfile createPlayerProfile(SocialConnectionData socialConnectionData) {
                    return null;
                }

                @Override
                public PlayerProfile createPlayerProfile(PlayerProfile playerProfile) {
                    return playerProfile;
                }
            };
        }
    }

}
