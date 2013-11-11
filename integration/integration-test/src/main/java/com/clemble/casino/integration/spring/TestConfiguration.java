package com.clemble.casino.integration.spring;

import static com.clemble.casino.server.spring.common.SpringConfiguration.INTEGRATION_CLOUD;
import static com.clemble.casino.server.spring.common.SpringConfiguration.INTEGRATION_DEFAULT;
import static com.clemble.casino.server.spring.common.SpringConfiguration.INTEGRATION_TEST;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth.common.signature.RSAKeySecret;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.client.error.ClembleCasinoRestErrorHandler;
import com.clemble.casino.configuration.ServerRegistryConfiguration;
import com.clemble.casino.integration.game.GameSessionPlayerFactory;
import com.clemble.casino.integration.game.IntegrationGameSessionPlayerFactory;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.payment.IntegrationPaymentTransactionOperations;
import com.clemble.casino.integration.payment.PaymentTransactionOperations;
import com.clemble.casino.integration.payment.WebPaymentTransactionOperations;
import com.clemble.casino.integration.player.IntegrationPlayerOperations;
import com.clemble.casino.integration.player.PlayerOperations;
import com.clemble.casino.integration.player.WebPlayerOperations;
import com.clemble.casino.integration.player.account.AccountOperations;
import com.clemble.casino.integration.player.account.IntegrationAccountOperations;
import com.clemble.casino.integration.player.account.WebAccountOperations;
import com.clemble.casino.integration.player.listener.PlayerListenerOperations;
import com.clemble.casino.integration.player.listener.SimplePlayerListenerOperations;
import com.clemble.casino.integration.player.profile.IntegrationProfileOperations;
import com.clemble.casino.integration.player.profile.ProfileOperations;
import com.clemble.casino.integration.player.profile.WebProfileOperations;
import com.clemble.casino.integration.player.session.IntegrationSessionOperations;
import com.clemble.casino.integration.player.session.SessionOperations;
import com.clemble.casino.integration.player.session.WebSessionOperations;
import com.clemble.casino.integration.spring.web.management.ManagementWebSpringConfiguration;
import com.clemble.casino.server.spring.common.JsonSpringConfiguration;
import com.clemble.casino.server.spring.common.ServerRegistrySpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.web.ClientRestCommonSpringConfiguration;
import com.clemble.casino.server.spring.web.payment.PaymentWebSpringConfiguration;
import com.clemble.casino.server.spring.web.player.PlayerWebSpringConfiguration;
import com.clemble.casino.server.web.management.PlayerRegistrationController;
import com.clemble.casino.server.web.management.PlayerSessionController;
import com.clemble.casino.server.web.payment.PaymentTransactionController;
import com.clemble.casino.server.web.player.PlayerProfileController;
import com.clemble.casino.server.web.player.account.PlayerAccountController;
import com.clemble.test.random.AbstractValueGenerator;
import com.clemble.test.random.ObjectGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Import(value = { JsonSpringConfiguration.class, TestConfiguration.LocalTestConfiguration.class, TestConfiguration.IntegrationTestConfiguration.class })
public class TestConfiguration {

    @Autowired
    @Qualifier("playerOperations")
    public PlayerOperations playerOperations;

    @PostConstruct
    public void initialize() {
        ObjectGenerator.register(RSAKeySecret.class, new AbstractValueGenerator<RSAKeySecret>() {

            @Override
            public RSAKeySecret generate() {
                try {
                    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
                    generator.initialize(1024);
                    KeyPair keyPair = generator.generateKeyPair();
                    return new RSAKeySecret(keyPair.getPrivate(), keyPair.getPublic());
                } catch (NoSuchAlgorithmException algorithmException) {
                    return null;
                }
            }
        });
    }

    @Bean
    @Singleton
    public GameScenarios gameScenarios() {
        return new GameScenarios(playerOperations);
    }

    @Configuration
    @Profile(value = SpringConfiguration.DEFAULT)
    @Import(value = { PaymentWebSpringConfiguration.class, PlayerWebSpringConfiguration.class, ManagementWebSpringConfiguration.class })
    public static class LocalTestConfiguration {

        @Autowired
        @Qualifier("objectMapper")
        public ObjectMapper objectMapper;

        @Autowired
        @Qualifier("playerRegistrationController")
        public PlayerRegistrationController playerRegistrationController;

        @Autowired
        @Qualifier("playerSessionController")
        public PlayerSessionController playerSessionController;

        @Autowired
        @Qualifier("playerAccountController")
        public PlayerAccountController playerAccountController;

        @Autowired
        public PlayerProfileController playerProfileController;

        @Autowired
        public PaymentTransactionController paymentTransactionController;

        @Bean
        @Singleton
        public PlayerListenerOperations playerListenerOperations() {
            return new SimplePlayerListenerOperations(objectMapper);
        }

        @Bean
        @Singleton
        public PlayerOperations playerOperations() {
            return new WebPlayerOperations(playerRegistrationController, sessionOperations(), accountOperations(), playerListenerOperations(),
                    playerProfileOperations());
        }

        @Bean
        @Singleton
        public AccountOperations accountOperations() {
            return new WebAccountOperations(paymentTransactionController, playerAccountController);
        }

        @Bean
        @Singleton
        public SessionOperations sessionOperations() {
            return new WebSessionOperations(playerSessionController);
        }

        @Bean
        @Singleton
        public ProfileOperations playerProfileOperations() {
            return new WebProfileOperations(playerProfileController);
        }

        @Bean
        @Singleton
        public PaymentTransactionOperations paymentTransactionOperations() {
            return new WebPaymentTransactionOperations(paymentTransactionController);
        }

    }

    @Configuration
    @Profile({ INTEGRATION_TEST, INTEGRATION_CLOUD, INTEGRATION_DEFAULT })
    @Import({ ClientRestCommonSpringConfiguration.class, ServerRegistrySpringConfiguration.class })
    public static class IntegrationTestConfiguration {

        @Autowired
        @Qualifier("objectMapper")
        public ObjectMapper objectMapper;

        @Value("#{systemProperties['clemble.casino.management.url'] ?: 'http://localhost:8080/integration-management-web/'}")
        public String baseUrl;

        @Autowired
        public ServerRegistryConfiguration serverRegistryConfiguration;

        public String getBaseUrl() {
            String base = baseUrl.substring(0, baseUrl.substring(0, baseUrl.length() - 1).lastIndexOf("/") + 1);
            return base;
        }

        @Bean
        @Singleton
        public PlayerListenerOperations playerListenerOperations() {
            return new SimplePlayerListenerOperations(objectMapper);
        }

        @Bean
        @Singleton
        public RestTemplate restTemplate() {
            RestTemplate restTemplate = new RestTemplate();

            for (HttpMessageConverter<?> messageConverter : restTemplate.getMessageConverters()) {
                if (messageConverter instanceof MappingJackson2HttpMessageConverter) {
                    ((MappingJackson2HttpMessageConverter) messageConverter).setObjectMapper(objectMapper);
                }
            }

            restTemplate.setErrorHandler(new ClembleCasinoRestErrorHandler(objectMapper));
            return restTemplate;
        }

        @Bean
        @Singleton
        public AccountOperations accountOperations() {
            return new IntegrationAccountOperations(restTemplate());
        }

        @Bean
        @Singleton
        public PlayerOperations playerOperations() {
            return new IntegrationPlayerOperations(baseUrl, restTemplate(), playerListenerOperations(), playerProfileOperations(), sessionOperations(),
                    accountOperations());
        }

        @Bean
        @Singleton
        public SessionOperations sessionOperations() {
            return new IntegrationSessionOperations(restTemplate(), baseUrl);
        }

        @Bean
        @Singleton
        public ProfileOperations playerProfileOperations() {
            return new IntegrationProfileOperations(restTemplate());
        }

        @Bean
        @Singleton
        public GameSessionPlayerFactory<?> genericGameSessionFactory() {
            return new IntegrationGameSessionPlayerFactory<>(restTemplate(), getBaseUrl());
        }

        @Bean
        @Singleton
        public PaymentTransactionOperations paymentTransactionOperations() {
            return new IntegrationPaymentTransactionOperations(restTemplate(), serverRegistryConfiguration.getPaymentRegistry());
        }

    }
}
