package com.clemble.casino.server.spring.common;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.server.ServerRegistry;
import com.clemble.casino.server.configuration.RestServerRegistryServerService;
import com.clemble.casino.server.configuration.ServerLocation;
import com.clemble.casino.server.configuration.ServerRegistryServerService;
import com.clemble.casino.server.player.notification.PlayerNotificationRegistry;
import com.clemble.casino.server.player.notification.SimplePlayerNotificationRegistry;
import com.clemble.casino.server.spring.web.ClientRestCommonSpringConfiguration;

@Configuration
@Import({ ServerRegistrySpringConfiguration.Cloud.class, ServerRegistrySpringConfiguration.Default.class,
        ServerRegistrySpringConfiguration.IntegrationTest.class, ServerRegistrySpringConfiguration.Test.class })
public class ServerRegistrySpringConfiguration implements SpringConfiguration {

    @Configuration
    @Import(ClientRestCommonSpringConfiguration.class)
    @Profile(value = { CLOUD })
    public static class Cloud {

        @Autowired
        public RestTemplate restTemplate;

        @Bean
        public ServerRegistryServerService serverRegistryService(@Value("${clemble.casino.management.url}") String url) {
            return new RestServerRegistryServerService(url, restTemplate);
        }

    }

    @Configuration
    @Import(ClientRestCommonSpringConfiguration.class)
    @Profile(value = { INTEGRATION_TEST })
    public static class IntegrationTest {

        @Autowired
        public RestTemplate restTemplate;

        @Bean
        public ServerRegistryServerService serverRegistryService(@Value("${clemble.casino.management.url?:http://localhost:9999/picpacpoe-management/}") String url) {
            return new RestServerRegistryServerService(url, restTemplate);
        }

    }

    @Configuration
    @Import(ClientRestCommonSpringConfiguration.class)
    @Profile(value = { DEFAULT })
    public static class Default {

        @Autowired
        public RestTemplate restTemplate;

        @Autowired(required = false)
        @Qualifier("realServerRegistryService")
        public ServerRegistryServerService realServerRegistryService;

        @Bean
        public ServerRegistryServerService serverRegistryService(@Value("${clemble.casino.management.url?:http://localhost:8080/picpacpoe-management/}") String url) {
            return realServerRegistryService != null ? realServerRegistryService : new RestServerRegistryServerService(url, restTemplate);
        }

    }

    @Configuration
    @Profile(value = { UNIT_TEST })
    public static class Test {

        @Autowired(required = false)
        @Qualifier("realServerRegistryService")
        public ServerRegistryServerService realServerRegistryService;

        @Bean
        public ServerRegistryServerService serverRegistryService() {
            return realServerRegistryService != null ? realServerRegistryService : new ServerRegistryServerService() {

                @Override
                public PlayerNotificationRegistry getPlayerNotificationRegistry() {
                    ServerRegistry serverRegistry = new ServerRegistry(Collections.singletonList("http://localhost"));
                    return new SimplePlayerNotificationRegistry(serverRegistry);
                }

                @Override
                public ServerLocation getPayment() {
                    return new ServerLocation("http://localhost:8080/payment-web/");
                }

            };
        }

    }

}
