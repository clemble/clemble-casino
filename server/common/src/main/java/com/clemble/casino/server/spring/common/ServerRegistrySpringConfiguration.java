package com.clemble.casino.server.spring.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.clemble.casino.configuration.ServerRegistryConfiguration;

@Configuration
@Import({ServerRegistrySpringConfiguration.Default.class, ServerRegistrySpringConfiguration.IntegrationTest.class})
public class ServerRegistrySpringConfiguration implements SpringConfiguration {

    @Configuration
    @Profile({ CLOUD, DEFAULT, INTEGRATION_CLOUD, UNIT_TEST })
    public static class Default {

        @Bean
        public ServerRegistryConfiguration serverRegistryConfiguration(
                @Value("#{systemProperties['clemble.casino.management.url'] ?: 'localhost'}") String baseUrl) {
            return new ServerRegistryConfiguration(baseUrl);
        }

    }

    @Configuration
    @Profile({ INTEGRATION_TEST, INTEGRATION_DEFAULT })
    public static class IntegrationTest {

        @Bean
        public ServerRegistryConfiguration serverRegistryConfiguration(@Value("#{systemProperties['clemble.casino.management.url'] ?: 'localhost'}") String baseUrl) {
            return new ServerRegistryConfiguration("localhost", "http://localhost:8080/player-web/", "http://localhost:8080/payment-web/", "http://localhost:8080/integration-game");
        }
    }

}
